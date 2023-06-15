package com.huakai.controller;

import com.google.common.util.concurrent.RateLimiter;
import com.google.gson.Gson;
import com.huakai.config.RedisService;
import com.huakai.controller.dto.ItemAmount;
import com.huakai.error.BussinesssError;
import com.huakai.error.ErrorEnum;
import com.huakai.mapper.dataobject.StockLogDO;
import com.huakai.mapper.dataobject.UserDO;
import com.huakai.mq.RocketmqProducer;
import com.huakai.response.CommonReturnType;
import com.huakai.service.OrderService;
import com.huakai.service.PromoService;
import com.huakai.service.StockLogService;
import com.huakai.util.CaptchaUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.*;

/**
 * @author: huakaimay
 * @since: 2021-09-02
 */
@RestController
@RequestMapping("/order")
@CrossOrigin(allowCredentials = "true", originPatterns = "*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private RedisService redisService;

    @Autowired
    private RocketmqProducer producer;

    @Autowired
    private StockLogService stockLogService;

    @Autowired
    private PromoService promoService;

    private ExecutorService executorService;

    private RateLimiter rateLimiter;

    @PostConstruct
    private void init() {
        // 每秒运行执行的次数，应该更具tps来创建一个合适的值，比如我们有两台服务800tps，那么保护性的可以设置350
        rateLimiter = RateLimiter.create(300);
        executorService = Executors.newFixedThreadPool(20);
    }

    @PostMapping("/createorder")
    public CommonReturnType createOrder(@RequestParam("itemId") Integer itemId,
                                        @RequestParam(value = "promoId", required = false) Integer promoId,
                                        @RequestParam("amount") Integer amount,
                                        @RequestParam(value = "promoToken", required = false) String promoToken,
                                        @RequestParam("token") String token) throws BussinesssError {

        if(!rateLimiter.tryAcquire())
            throw new BussinesssError(ErrorEnum.SYSTEM_BUSY);

        if (StringUtils.isEmpty(token))
            throw new BussinesssError(ErrorEnum.USER_NOT_LOGIN);

        String userDOStr = redisService.get(token);
        // Boolean isLogin = (Boolean)request.getSession().getAttribute("isLogin");
        if (ObjectUtils.isEmpty(userDOStr))
            throw new BussinesssError(ErrorEnum.USER_NOT_LOGIN);

        // UserDO userDO = (UserDO)request.getSession().getAttribute("loginUser");

        UserDO userDO = new Gson().fromJson(userDOStr, UserDO.class);
        // OrderDo orderDo = orderService.createOrder(userDO.getId(), itemId, promoId, amount);

        // 秒杀活动不是所有的商品都有的，所有promotoken是非必填
        if (promoToken != null) {
            String promoTokenInCache = redisService.get("promo_token_" + promoId + "_itemId_ " + itemId + "_userId_" + userDO.getId());
            if (promoTokenInCache == null) {
                System.out.println("缓存没有token");
                throw new BussinesssError(ErrorEnum.USER_NOT_LOGIN);
            }

            if (!promoToken.equals(promoTokenInCache))
                throw new BussinesssError(ErrorEnum.PARAMTER_VALIDATION_ERROR, "秒杀令牌错误");
        }

        Future<Object> future = executorService.submit(() -> {
            // 处理库存请求
            handleStockRequest(itemId, promoId, amount, userDO.getId());
            return null;
        });

        try {
            future.get();
        } catch (InterruptedException e) {
            throw new BussinesssError(ErrorEnum.UNKNOWN_ERROR);
        } catch (ExecutionException e) {
            throw new BussinesssError(ErrorEnum.UNKNOWN_ERROR);
        }


        return CommonReturnType.create(null);
    }

    @PostMapping("/generateToken")
    public CommonReturnType generateToken(@RequestParam("itemId") Integer itemId,
                                          @RequestParam(value = "promoId", required = false) Integer promoId,
                                          @RequestParam("token") String token,
                                          @RequestParam("verifyCode") String verifyCode) throws BussinesssError {

        if (StringUtils.isEmpty(token))
            throw new BussinesssError(ErrorEnum.USER_NOT_LOGIN);

        String userDOStr = redisService.get(token);
        if (ObjectUtils.isEmpty(userDOStr))
            throw new BussinesssError(ErrorEnum.USER_NOT_LOGIN);


        UserDO userDO = new Gson().fromJson(userDOStr, UserDO.class);

        String vCode = redisService.get("verify_code_user_" + userDO.getId());
        if (vCode == null)
            throw new BussinesssError(ErrorEnum.VERIFY_CODE_ERROR);

        if (!verifyCode.equals(vCode))
            throw new BussinesssError(ErrorEnum.VERIFY_CODE_ERROR);

        // 有活动时生成令牌
        if (promoId != null) {

            // 校验并生成令牌
            String promoToken = promoService.generateScToken(promoId, itemId, userDO.getId());
            // 因为活动尚未开始导致生成令牌时没有生成redis所以这边直接显示未登录
            if (promoToken == null) {
                throw new BussinesssError(ErrorEnum.PROMO_TOKEN_ERROR);
            }

            // 大闸数量扣减
            redisService.decrement("promo_door_count_" + itemId, 1);
            return CommonReturnType.create(promoToken);
        }

        return CommonReturnType.create(null);
    }


    @GetMapping("/generateverifycode")
    public void generateverifycode(@RequestParam("token") String token, HttpServletResponse response) throws BussinesssError, IOException {
        if (StringUtils.isEmpty(token))
            throw new BussinesssError(ErrorEnum.USER_NOT_LOGIN);

        String userDOStr = redisService.get(token);
        if (ObjectUtils.isEmpty(userDOStr))
            throw new BussinesssError(ErrorEnum.USER_NOT_LOGIN);

        // 生成验证码
        String code = CaptchaUtil.generateCaptchaString();

        // 验证码存五分钟
        redisService.put("verify_code_user_" + new Gson().fromJson(userDOStr, UserDO.class).getId(), code, 5, TimeUnit.MINUTES);
        System.out.printf("验证码：%s%n", code);
        // 生成图片
        BufferedImage captchaImage = CaptchaUtil.createCaptchaImage(code);
        CaptchaUtil.saveCaptchaImage(captchaImage, response.getOutputStream());
    }


    @Transactional(rollbackFor = Exception.class)
    private void handleStockRequest(int itemId, int promoId, int amount, int userId) throws BussinesssError {

        if (redisService.hasKey("promo_stock_zero"))
            throw new BussinesssError(ErrorEnum.STOCK_NOT_ENOUGH);


        // 生成流水前判断库存是否充足，没有判断会导致多余的数据生成
        String stockLogId = UUID.randomUUID().toString().replace("-", "");
        // 提前生成库存入库流水
        StockLogDO stockLogDO = new StockLogDO();
        stockLogDO.setStockLogId(stockLogId);
        stockLogDO.setAmount(amount);
        stockLogDO.setItemId(itemId);
        // 1：初始化状态，2：下单扣减库存成功，3：下单回滚
        stockLogDO.setStatus((byte) 1);
        stockLogService.createStock(stockLogDO);


        // 发送MQ消息
        ItemAmount itemAmount = new ItemAmount();
        itemAmount.setUserId(userId);
        itemAmount.setId(itemId);
        itemAmount.setPromoId(promoId);
        itemAmount.setAmount(amount);
        itemAmount.setStockLogId(stockLogId);

        if (!producer.sendMessageInTransaction("stock", new Gson().toJson(itemAmount))) {
            String key = "promo_item_stock_" + itemAmount.getId();
            redisService.increment(key, itemAmount.getAmount());
            throw new BussinesssError(ErrorEnum.MQ_SEND_FAIL);
        }
    }

}

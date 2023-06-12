package com.huakai.controller;

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
import com.huakai.service.StockLogService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

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

    @PostMapping("/createorder")
    public CommonReturnType createOrder(@RequestParam("itemId") Integer itemId,
                                        @RequestParam(value = "promoId", required = false) Integer promoId,
                                        @RequestParam("amount") Integer amount,
                                        @RequestParam("token") String token) throws BussinesssError {


        if (StringUtils.isEmpty(token))
            throw new BussinesssError(ErrorEnum.USER_NOT_LOGIN);

        String userDOStr = redisService.get(token);
        // Boolean isLogin = (Boolean)request.getSession().getAttribute("isLogin");
        if (ObjectUtils.isEmpty(userDOStr))
            throw new BussinesssError(ErrorEnum.USER_NOT_LOGIN);

        // UserDO userDO = (UserDO)request.getSession().getAttribute("loginUser");

        UserDO userDO = new Gson().fromJson(userDOStr, UserDO.class);
        // OrderDo orderDo = orderService.createOrder(userDO.getId(), itemId, promoId, amount);

        // 处理库存请求
        handleStockRequest(itemId, promoId, amount, userDO.getId());

        return CommonReturnType.create(null);
    }


    @Transactional(rollbackFor = Exception.class)
    private void handleStockRequest(int itemId, int promoId, int amount, int userId) throws BussinesssError {

        if(redisService.hasKey("promo_stock_zero"))
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

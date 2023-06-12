package com.huakai.service.impl;

import com.google.gson.Gson;
import com.huakai.config.RedisService;
import com.huakai.controller.dto.ItemDto;
import com.huakai.error.BussinesssError;
import com.huakai.error.ErrorEnum;
import com.huakai.mapper.OrderDoMapper;
import com.huakai.mapper.SequenceDOMapper;
import com.huakai.mapper.StockLogDOMapper;
import com.huakai.mapper.UserDOMapper;
import com.huakai.mapper.dataobject.OrderDo;
import com.huakai.mapper.dataobject.SequenceDO;
import com.huakai.mapper.dataobject.StockLogDO;
import com.huakai.mq.RocketmqProducer;
import com.huakai.service.ItemService;
import com.huakai.service.OrderService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

/**
 * @author: huakaimay
 * @since: 2021-09-02
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private UserDOMapper userDOMapper;

    @Autowired
    private ItemService itemService;

    @Autowired
    private OrderDoMapper orderDoMapper;

    @Autowired
    private SequenceDOMapper sequenceDOMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private RocketmqProducer producer;

    @Autowired
    private StockLogDOMapper stockLogDOMapper;


    @Override
    @Transactional
    public OrderDo createOrder(Integer uesrId, Integer itemId, Integer promoId, Integer amount, String stockLogId) throws BussinesssError {
        // 参数校验
        if (amount < 1) {
            throw new BussinesssError(ErrorEnum.PARAMTER_VALIDATION_ERROR, "购买数量有误");
        }

        ItemDto itemDto = itemInCache(itemId);
        if (itemDto == null)
            throw new BussinesssError(ErrorEnum.ITEM_NOT_EXIST);

        if (promoId != null) {
            if (itemDto.getPromoDto().getId() != promoId)
                throw new BussinesssError(ErrorEnum.PARAMTER_VALIDATION_ERROR, "活动信息不正确");
            else if (itemDto.getPromoDto().getStatus() != 2)
                throw new BussinesssError(ErrorEnum.PARAMTER_VALIDATION_ERROR, "活动信息不正确");
        }

        // 落单减库存这边需要先发布
        boolean success = itemService.decreaseStock(itemId, amount);
        if (!success) {
            throw new BussinesssError(ErrorEnum.STOCK_NOT_ENOUGH);
        }


        // insert into order
        /*
        如果减库存异步发送之后，后续的逻辑出现问题事物回滚，而异步消息已经被消费导致库存不正确
         */
        OrderDo orderDo = new OrderDo();
        orderDo.setId(generatorOrderNo());
        orderDo.setItemId(itemId);
        orderDo.setAmount(amount);
        orderDo.setUserId(uesrId);

        if (promoId != null) {
            orderDo.setPromoId(promoId);
            orderDo.setPrice(itemDto.getPromoDto().getPrice());
        } else {
            orderDo.setPrice(itemDto.getPrice());
        }

        // 下面两个操作都可以异步化
        orderDo.setTotalPrice(orderDo.getPrice().multiply(new BigDecimal(amount.intValue())));
        orderDoMapper.insertSelective(orderDo);

        // 商品增加商品销量
        itemService.increaseStock(itemId, amount);

        // 将异步放到最后发送，这样的问题是事物还未结束，若事物发生异常前消息已经被消费导致数据不一致
        // String key = "promo_item_stock_" + itemId;
        // try {
        //     ItemAmount item = new ItemAmount();
        //     item.setId(itemId);
        //     item.setAmount(amount);
        //     producer.sendMessage("stock", new Gson().toJson(item));
        // } catch (Exception e) {
        //     // 如果 RocketMQ 发送失败，则将库存增加回去
        //     redisService.increment(key, amount);
        // }

        // 事物commit可能异常

        // 修改流水状态为成功
        StockLogDO stockLogDO = new StockLogDO();
        stockLogDO.setStockLogId(stockLogId);
        // 1：初始化状态，2：下单扣减库存成功，3：下单回滚
        stockLogDO.setStatus((byte) 2);
        stockLogDOMapper.updateByPrimaryKeySelective(stockLogDO);

        // 返回订单
        return orderDo;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private String generatorOrderNo() {
        // 订单号有16位
        StringBuilder stringBuilder = new StringBuilder();
        // 前8位为时间信息，年月日
        LocalDateTime now = LocalDateTime.now();
        String nowDate = now.format(DateTimeFormatter.ISO_DATE).replace("-", "");
        stringBuilder.append(nowDate);

        // 中间六位为自增序列
        // 获取当前sequence
        int sequence = 0;
        SequenceDO sequenceDO = sequenceDOMapper.selectByPrimaryKey("order_info");
        sequence = sequenceDO.getCurrentValue();
        sequenceDO.setCurrentValue(sequenceDO.getCurrentValue() + sequenceDO.getStep());
        sequenceDOMapper.updateByPrimaryKeySelective(sequenceDO);
        // 凑足六位
        String sequenceStr = String.valueOf(sequence);
        for (int i = 0; i < 6 - sequenceStr.length(); i++) {
            stringBuilder.append(i);
        }
        stringBuilder.append(sequenceStr);

        // 最后两位分库分表位
        stringBuilder.append("00");
        return stringBuilder.toString();
    }



    /**
     * 缓存商品信息
     */
    private ItemDto itemInCache(Integer id) {

        String cacheKey = "item_valited_" + id;
        ItemDto userDO = redisService.get(cacheKey, ItemDto.class);

        if(ObjectUtils.isEmpty(userDO)) {
            userDO = itemService.getItemDetailById(id);
            redisService.put(cacheKey, new Gson().toJson(userDO), 10, TimeUnit.MINUTES);
        }

        return userDO;
    }

}

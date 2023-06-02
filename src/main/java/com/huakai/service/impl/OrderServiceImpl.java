package com.huakai.service.impl;

import com.google.gson.Gson;
import com.huakai.config.RedisService;
import com.huakai.controller.dto.ItemDto;
import com.huakai.error.BussinesssError;
import com.huakai.error.ErrorEnum;
import com.huakai.mapper.OrderDoMapper;
import com.huakai.mapper.SequenceDOMapper;
import com.huakai.mapper.UserDOMapper;
import com.huakai.mapper.dataobject.OrderDo;
import com.huakai.mapper.dataobject.SequenceDO;
import com.huakai.mapper.dataobject.UserDO;
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

    @Override
    @Transactional
    public OrderDo createOrder(Integer uesrId, Integer itemId, Integer promoId, Integer amount) throws BussinesssError {
        // 参数校验
        if (amount < 1) {
            throw new BussinesssError(ErrorEnum.PARAMTER_VALIDATION_ERROR, "购买数量有误");
        }

        UserDO userDO = userInCache(uesrId);
        if (userDO == null)
            throw new BussinesssError(ErrorEnum.USER_NOT_EXIST);

        ItemDto itemDto = itemInCache(itemId);
        if (itemDto == null)
            throw new BussinesssError(ErrorEnum.ITEM_NOT_EXIST);

        // 落单减库存
        boolean success = itemService.decreaseStock(itemId, amount);
        if (!success) {
            throw new BussinesssError(ErrorEnum.STOCK_NOT_ENOUGH);
        }

        if (promoId != null) {
            if (itemDto.getPromoDto().getId() != promoId)
                throw new BussinesssError(ErrorEnum.PARAMTER_VALIDATION_ERROR, "活动信息不正确");
            else if (itemDto.getPromoDto().getStatus() != 2)
                throw new BussinesssError(ErrorEnum.PARAMTER_VALIDATION_ERROR, "活动信息不正确");
        }

        // insert into order
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

        orderDo.setTotalPrice(orderDo.getPrice().multiply(new BigDecimal(amount.intValue())));
        orderDoMapper.insertSelective(orderDo);

        // 商品增加商品销量
        itemService.increaseStock(itemId, amount);

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
     * 缓存用户信息
     */
    private UserDO userInCache(Integer uesrId) {

        String cacheKey = "user_valited_" + uesrId;
        UserDO userDO = redisService.get(cacheKey, UserDO.class);

        if(ObjectUtils.isEmpty(userDO)) {
            userDO = userDOMapper.selectByPrimaryKey(uesrId);
            redisService.put(cacheKey, new Gson().toJson(userDO), 10, TimeUnit.MINUTES);
        }

        return userDO;
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

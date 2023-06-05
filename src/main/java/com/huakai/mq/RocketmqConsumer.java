package com.huakai.mq;

import com.google.gson.Gson;
import com.huakai.controller.dto.ItemAmount;
import com.huakai.mapper.ItemStockDOMapper;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 正常情况下consumer应该有一个单独服务来接收，这边是联系项目就不开项目做
 * @author: huakaimay
 * @since: 2023-06-05
 */
@Service
@RocketMQMessageListener(consumerGroup = "stock_consumter_group", topic = "stock")
public class RocketmqConsumer implements RocketMQListener<String> {


    @Autowired
    private ItemStockDOMapper itemStockDOMapper;

    @Override
    public void onMessage(String s) {
        System.out.println("receive msg: " + s);

        Gson gson = new Gson();
        ItemAmount itemAmount = gson.fromJson(s, ItemAmount.class);
        itemStockDOMapper.decreaseStock(itemAmount.getId(), itemAmount.getAmount());
        System.out.println("减库存成功");
    }
}

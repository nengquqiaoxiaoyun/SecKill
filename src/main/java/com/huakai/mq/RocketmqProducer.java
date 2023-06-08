package com.huakai.mq;

import com.google.gson.Gson;
import com.huakai.config.RedisService;
import com.huakai.controller.dto.ItemAmount;
import com.huakai.error.BussinesssError;
import com.huakai.excep.RocketMQSendFailedException;
import com.huakai.service.OrderService;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.nio.charset.Charset;

/**
 * @author: huakaimay
 * @since: 2023-06-05
 */
@Service
public class RocketmqProducer {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    private TransactionMQProducer producer;

    @Autowired
    private RedisService redisService;

    @Autowired
    private OrderService orderService;

    @PostConstruct
    public void init() throws Exception {
        producer = new TransactionMQProducer("group_name");
        producer.setNamesrvAddr("192.168.127.131:9876");
        producer.setTransactionListener(new TransactionListenerImpl());
        producer.start();
    }

    public void sendMessage(String topic, String message) {
        rocketMQTemplate.asyncSend(topic, message, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                // 消息发送成功后的逻辑处理
                System.out.println("消息发送成功：" + sendResult);
            }

            @Override
            public void onException(Throwable throwable) {
                // 消息发送失败后的逻辑处理
                System.out.println("消息发送失败：" + throwable.getMessage());
                throw new RocketMQSendFailedException(throwable.getMessage());
            }
        });
    }

    public boolean sendMessageInTransaction(String topic, String message){
        Message msg = new Message(topic, message.getBytes(Charset.forName("UTF-8")));
        TransactionSendResult sendResult = null;
        try {
            // 这边的事物针对的是具体的业务逻辑实现executeLocalTransaction方法中的逻辑
            sendResult = producer.sendMessageInTransaction(msg, message);
            // 上面代码之后的逻辑是不能处理事物的
        } catch (MQClientException e) {
            e.printStackTrace();
            // 异步消息发送失败，回滚redis库存
            ItemAmount itemAmount = new Gson().fromJson(message, ItemAmount.class);
            String key = "promo_item_stock_" + itemAmount.getId();
            redisService.increment(key, itemAmount.getAmount());
        }
        System.out.printf("发送结果：%s%n, 事物状态：%s%n", sendResult.getSendStatus(), sendResult.getLocalTransactionState());

        if(sendResult.getLocalTransactionState() == LocalTransactionState.COMMIT_MESSAGE)
            return true;

        return false;
    }

     class TransactionListenerImpl implements TransactionListener {
        @Override
        public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
            // TODO 执行本地事务逻辑，返回 COMMIT_MESSAGE 或 ROLLBACK_MESSAGE 或 UNKNOW
            String jsMsg = (String) arg;
            ItemAmount itemAmount = new Gson().fromJson(jsMsg, ItemAmount.class);
            try {
                orderService.createOrder(itemAmount.getUserId(), itemAmount.getId(), itemAmount.getPromoId(), itemAmount.getAmount());
            } catch (BussinesssError e) {
                return LocalTransactionState.ROLLBACK_MESSAGE;
            }
            return LocalTransactionState.COMMIT_MESSAGE;
        }

        /**
         * 检查本地事务状态
         */
        @Override
        public LocalTransactionState checkLocalTransaction(MessageExt msg) {
            // 判断库存是否扣减成功，由此判断返回状态，若扣减成功则返回成功，为扣减成功回滚
            String jsonMsg = new String(msg.getBody());
            ItemAmount itemAmount = new Gson().fromJson(jsonMsg, ItemAmount.class);


            return LocalTransactionState.COMMIT_MESSAGE;
        }
    }
}

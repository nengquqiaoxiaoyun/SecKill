package com.huakai.mq;

import com.huakai.excep.RocketMQSendFailedException;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: huakaimay
 * @since: 2023-06-05
 */
@Service
public class RocketmqProducer {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;


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
}

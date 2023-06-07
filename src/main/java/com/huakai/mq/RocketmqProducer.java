package com.huakai.mq;

import com.huakai.excep.RocketMQSendFailedException;
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

    public void sendMessageInTransaction(String topic, String message) throws Exception {
        Message msg = new Message(topic, message.getBytes(Charset.forName("UTF-8")));
        SendResult sendResult = producer.sendMessageInTransaction(msg, null);
        System.out.printf("发送结果：%s%n", sendResult.getSendStatus());
    }

    static class TransactionListenerImpl implements TransactionListener {
        @Override
        public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
            // TODO 执行本地事务逻辑，返回 COMMIT_MESSAGE 或 ROLLBACK_MESSAGE 或 UNKNOW
            return LocalTransactionState.COMMIT_MESSAGE;
        }

        @Override
        public LocalTransactionState checkLocalTransaction(MessageExt msg) {
            // TODO 检查本地事务状态，返回 COMMIT_MESSAGE 或 ROLLBACK_MESSAGE 或 UNKNOW
            return LocalTransactionState.COMMIT_MESSAGE;
        }
    }
}

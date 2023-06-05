package com.huakai.excep;

public class RocketMQSendFailedException extends RuntimeException {
    public RocketMQSendFailedException(String message) {
        super(message);
    }
}

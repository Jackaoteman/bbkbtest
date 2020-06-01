package com.liyue.liyuetest.listener;

public interface IMessageHandleConsumer {
    void  handlMessage(String message);
    String  getQueueName();
}

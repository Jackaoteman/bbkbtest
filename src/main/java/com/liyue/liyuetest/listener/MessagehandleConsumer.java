package com.liyue.liyuetest.listener;

import org.springframework.stereotype.Component;

@Component
public class MessagehandleConsumer implements  IMessageHandleConsumer {

    public  static  final String  QueueName="bababus.com";

    @Override
    public void handlMessage(String message) {
        System.out.println("监听到的消息："+message);
    }

    @Override
    public String getQueueName() {
        return QueueName;
    }
}

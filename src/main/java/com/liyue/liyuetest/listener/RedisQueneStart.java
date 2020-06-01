package com.liyue.liyuetest.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
//@DependsOn(value = "")//加载bean的顺序
public class RedisQueneStart implements ApplicationListener<ContextClosedEvent>, CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RedisQueueConsumerListener redisQueueConsumerListener;

    @Autowired
    private  MessagehandleConsumer messagehandleConsumer;


    @Override
    public void run(String... args) throws Exception {
        logger.info("RedisQueneStart afterPropertiesSet");
        List<IMessageHandleConsumer> consumers = new ArrayList<>();
        consumers.add(messagehandleConsumer);
        redisQueueConsumerListener.setConsumers(consumers);
        redisQueueConsumerListener.startListener();
        logger.info("redisQueueConsumerListener容器启动");
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent contextClosedEvent) {
        redisQueueConsumerListener.stopListener();
        logger.info("redisQueueConsumerListener容器停止");
    }
}

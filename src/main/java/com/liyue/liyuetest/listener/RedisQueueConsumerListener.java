package com.liyue.liyuetest.listener;

import com.liyue.liyuetest.Utils.JedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class RedisQueueConsumerListener {
    private static Logger logger = LoggerFactory.getLogger(RedisQueueConsumerListener.class);

    private volatile boolean exit = false;
    private List<IMessageHandleConsumer> consumers = new ArrayList<>();



    public void setConsumers(List<IMessageHandleConsumer> consumers) {
        this.consumers = consumers;
    }

    /**
     * 启动监听
     */
    public  void startListener(){
        for (IMessageHandleConsumer consumer : consumers) {
            MessageThread messageThread=new MessageThread(consumer);
            messageThread.start();
        }
    }

    /**
     * 容器
     */
    public void stopListener() {
        exit = true;
    }

    class MessageThread extends Thread{
         private IMessageHandleConsumer consumer;

      MessageThread(IMessageHandleConsumer consumer) {
            this.consumer = consumer;
        }

        @Override
        public void run() {
          logger.info("开始监听队列.................................");
            try {
                while (!exit) {
                    try {
                        String message = JedisUtil.get(consumer.getQueueName());
                        if (!StringUtils.isEmpty(message)) {
                            consumer.handlMessage(message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error("message handle error!queue:[]", consumer.getQueueName());
                    }
                }
            } catch (Exception e) {
                logger.error("RedisMessageConsumerThread error! exception:[]", e.getMessage());
            }
        }

        }
    }


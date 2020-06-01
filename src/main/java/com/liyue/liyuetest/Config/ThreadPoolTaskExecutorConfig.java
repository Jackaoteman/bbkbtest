package com.liyue.liyuetest.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**  拒绝策略：
 * rejectedExectutionHandler参数字段用于配置绝策略，常用拒绝策略如下
 * AbortPolicy：用于被拒绝任务的处理程序，它将抛出RejectedExecutionException
 * CallerRunsPolicy：用于被拒绝任务的处理程序，它直接在execute方法的调用线程中运行被拒绝的任务。
 * DiscardOldestPolicy：用于被拒绝任务的处理程序，它放弃最旧的未处理请求，然后重试execute。
 * DiscardPolicy：用于被拒绝任务的处理程序，默认情况下它将丢弃被拒绝的任务
 * ————————————————
 * 1.当一个任务被提交到线程池时，首先查看线程池的核心线程是否都在执行任务，否就选择一条线程执行任务，就是执行第二步。
 *
 * 2.查看核心线程池是否已满，不满就创建一条线程执行任务，否则执行第三步。
 *
 * 3.查看任务队列是否已满，不满就将任务存储在任务队列中，否则执行第四步。
 *
 * 4.查看线程池是否已满，不满就创建一条线程执行任务，否则就按照策略处理无法执行的任务
 * ————————————————
 * 如果当前运行的线程数小于corePoolSize，那么就创建线程来执行任务（执行时需要获取全局锁）。
 * 如果运行的线程大于或等于corePoolSize，那么就把task加入BlockQueue。
 * 如果创建的线程数量大于BlockQueue的最大容量，那么创建新线程来执行该任务。
 * 如果创建线程导致当前运行的线程数超过maximumPoolSize，就根据饱和策略来拒绝该任务。
 * ————————————————
 */
@Configuration
public class ThreadPoolTaskExecutorConfig {

    @Bean
    public ThreadPoolTaskExecutor initThreadPoolTaskExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("ThreadTask-Executor");
        executor.setCorePoolSize(20);
        executor.setMaxPoolSize(200);
        //队列大小
        executor.setQueueCapacity(100);
        //设置在关闭线程池时是否等待任务完成
        executor.setWaitForTasksToCompleteOnShutdown(true);
        //拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }


    @Bean
    public  ThreadPoolExecutor initThreadPoolExecutor(){
        ThreadPoolExecutor threadPoolExecutor=new ThreadPoolExecutor(10,20,100, TimeUnit.SECONDS,new LinkedBlockingDeque<>(10),new ThreadPoolExecutor.CallerRunsPolicy());
        return  threadPoolExecutor;
    }



}

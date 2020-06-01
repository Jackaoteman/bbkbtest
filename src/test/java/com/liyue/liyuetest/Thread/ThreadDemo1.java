package com.liyue.liyuetest.Thread;


import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadDemo1 {
    public static void main(String[] args) {


        ThreadPoolTaskExecutor  executor=new ThreadPoolTaskExecutor();

        int poolSize = executor.getPoolSize();
        int corePoolSize = executor.getCorePoolSize();
        int maxPoolSize = executor.getMaxPoolSize();
        System.out.println(poolSize);
        System.out.println(corePoolSize);
        System.out.println(maxPoolSize);


        ThreadPoolExecutor threadPoolExecutor=new ThreadPoolExecutor(5,10,100, TimeUnit.SECONDS,new LinkedBlockingDeque<>(5));

        //threadPoolExecutor.execute();

        //FixedThreadPool fixedThreadPool = new FixedThreadPool();


    }
}

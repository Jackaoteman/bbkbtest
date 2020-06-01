package com.liyue.liyuetest.Cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;

public class CacheTest {
    public static void main(String[] args) {

        CacheBuilder<Object, Object> cache = CacheBuilder.newBuilder().maximumSize(100).expireAfterWrite(3, TimeUnit.SECONDS);
        Cache<String, String> objectCache = cache.build();
        objectCache.put("name","zhangsan");

        String value = objectCache.getIfPresent("name");
        System.out.println(value);
        System.out.println("睡眠3秒之后");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String name = objectCache.getIfPresent("name");
        System.out.println("name="+name);


    }

}

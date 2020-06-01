package com.liyue.liyuetest.HashMap;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class HashMapDemo01 {

    public static void main(String[] args) throws InterruptedException {

      //  HashMap map=new HashMap();

        final HashMap<String, String> map = new HashMap<String, String>(2);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10000; i++) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            map.put(UUID.randomUUID().toString(), "");
                        }
                    }, "test" + i).start();
                }
            }
        }, "test");
        t.start();
        t.join();
       // Map map1 = Collections.synchronizedMap(map);


        ConcurrentHashMap concurrentHashMap=new ConcurrentHashMap();



    }


}

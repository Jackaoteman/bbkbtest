package com.liyue.liyuetest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ConcurrentHashMap;

@SpringBootTest
class LiyuetestApplicationTests {

    @Test
    void contextLoads() {
        ConcurrentHashMap<String,Integer> map=new ConcurrentHashMap<>();
        map.put("1",1);
    }

}

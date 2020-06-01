package com.liyue.liyuetest;

import com.liyue.liyuetest.Utils.JedisUtil;
import org.junit.jupiter.api.Test;

public class ApplicationDemo1 extends  BaseTest {

    @Test
    public  void t1(){

        JedisUtil.add("username","zhangsan");
        String username = JedisUtil.get("username");
        System.out.println(username);
     //   JedisUtil.remove("username");
    }


}

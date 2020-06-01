package com.liyue.liyuetest;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
@MapperScan(basePackages="com.liyue.liyuetest.Dao")
@ServletComponentScan("com.liyue.liyuetest.Filter")
//@EnableEurekaServer
public class LiyuetestApplication {

    public static void main(String[] args) {
        SpringApplication.run(LiyuetestApplication.class, args);
    }

}

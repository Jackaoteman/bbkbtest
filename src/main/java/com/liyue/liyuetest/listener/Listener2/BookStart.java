package com.liyue.liyuetest.listener.Listener2;

import org.springframework.stereotype.Service;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
@Service
public class BookStart implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("我爱学习");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}

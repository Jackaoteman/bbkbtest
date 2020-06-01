package com.liyue.liyuetest.Filter;

import com.alibaba.fastjson.JSON;
import com.liyue.liyuetest.Common.Result;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import springfox.documentation.spring.web.json.Json;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class HomeOpenInterceptor extends  HandlerInterceptorAdapter {


    @Override
    public  boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean flag=true;
        if (flag){
            System.out.println("请求路径="+request.getRequestURL());
            Result result=new Result();
            result.setCode("999");
            result.setMsg("出现异常");
            result.setSuccess(false);
            result.setData(null);
            String json= JSON.toJSONString(result);
            response.setContentType("application/json; charset=utf-8");
            try {
                response.getWriter().write(json);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return  false;
        }
        return  true;

    }
}

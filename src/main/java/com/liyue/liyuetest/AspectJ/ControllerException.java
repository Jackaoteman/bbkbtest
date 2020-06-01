package com.liyue.liyuetest.AspectJ;

import com.liyue.liyuetest.Common.Result;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//@ControllerAdvice
public class ControllerException {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex){
        StringBuffer requestURI = request.getRequestURL();
        System.out.println("出现异常"+ex);
        return  new Result(false,"999","系统正在努力抢修中",null);
    }



}

package com.liyue.liyuetest.Filter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
@Configuration
public class HomeOpenHandlerConfigration extends WebMvcConfigurerAdapter {

    //关键，将拦截器作为bean写入配置中
    @Bean
    public HomeOpenInterceptor myInterceptor(){
        return new HomeOpenInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(myInterceptor()).addPathPatterns("/book/**")
                .excludePathPatterns("/**").excludePathPatterns("/**");
        super.addInterceptors(registry);
    }

}

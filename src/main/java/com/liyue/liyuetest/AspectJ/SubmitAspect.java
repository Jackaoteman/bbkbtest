package com.liyue.liyuetest.AspectJ;


import org.springframework.core.annotation.AnnotationUtils;
import com.alibaba.druid.util.StringUtils;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.liyue.liyuetest.Common.Respon;
import com.liyue.liyuetest.Interface.Commit;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;


import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Aspect
//@Configuration
public class SubmitAspect {

    private static final Cache<String, Object> CACHES = CacheBuilder.newBuilder()
            // 最大缓存 100 个
            .maximumSize(100)
            // 设置缓存过期时间为S
            .expireAfterWrite(10, TimeUnit.SECONDS)
            .build();
//    private  static final Map<String,Object> CACHES=new ConcurrentHashMap<>(100);

    @Around("execution(public * *(..)) && @annotation(com.liyue.liyuetest.Interface.Commit)")
    public Object interceptor(ProceedingJoinPoint pjp) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        Commit form= method.getAnnotation(Commit.class);
        String key = getCacheKey(method, pjp.getArgs());

        if (!StringUtils.isEmpty(key)) {
            if (CACHES.getIfPresent(key) != null) {
                String name = method.getName();
                //Commit annotation = AnnotationUtil.getAnnotation(name, Commit.class);
                Respon resultResponse = new Respon();
                resultResponse.setData(null);
                resultResponse.setMsg("请勿重复请求");
                resultResponse.setCode("405");
                return  resultResponse;

            }
            // 如果是第一次请求,就将key存入缓存中
            if(form==null) {
                CACHES.put(key, key);
            }
        }
        try {
            return pjp.proceed();
        } catch (Throwable throwable) {
            throw new RuntimeException("服务器异常");
        } finally {
        }
    }

    /**
     *将来还要加上用户的唯一标识
     */
    private String getCacheKey(Method method, Object[] args) {
        return method.getName() + args[0];
    }
}

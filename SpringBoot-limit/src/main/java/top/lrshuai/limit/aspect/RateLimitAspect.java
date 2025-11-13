package top.lrshuai.limit.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.lrshuai.limit.annotation.RequestLimit;
import top.lrshuai.limit.common.ApiResultEnum;
import top.lrshuai.limit.common.R;
import top.lrshuai.limit.util.IpUtil;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
@Slf4j
public class RateLimitAspect {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    /**
     * 环绕通知，切入所有被@RateLimit注解标记的方法
     * “@annotation(requestLimit)” 只匹配方法上的
     * “@within(requestLimit)” 匹配类上的
     */
    @Around("(@annotation(requestLimit) || @within(requestLimit))")
    public Object around(ProceedingJoinPoint joinPoint, RequestLimit requestLimit) throws Throwable {

        // 获取HttpServletRequest对象，从而拿到客户端IP
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            // 非Web请求，直接放行
            return joinPoint.proceed();
        }
        HttpServletRequest request = attributes.getRequest();
        // 获取客户端真实IP的方法
        String ip = IpUtil.getClientIpAddress(request);

        // 优先从方法上获取注解
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        requestLimit = getTagAnnotation(method, RequestLimit.class);


        // 构建Redis的key，格式为：rate_limit:接口key:IP
        String methodName = method.getName();
        String key = requestLimit.key().isEmpty() ? methodName : requestLimit.key();
        String redisKey = "rate_limit:" + key + ":" + ip;

        // 操作Redis，进行计数和判断
        ValueOperations<String, Object> valueOps = redisTemplate.opsForValue();
        Integer currentCount = (Integer) valueOps.get(redisKey);

        if (currentCount == null) {
            // 第一次访问，设置key，初始值为1，并设置过期时间
            valueOps.set(redisKey, 1, requestLimit.second(), TimeUnit.SECONDS);
        } else if (currentCount < requestLimit.maxCount()) {
            // 计数未达到阈值，计数器+1 (注意：这里Redis的过期时间保持不变)
            valueOps.increment(redisKey);
        } else {
            // 计数已达到或超过阈值，抛出异常或返回错误信息
            log.warn("IP【{}】访问接口【{}】过于频繁，已被限流", ip, methodName);
            return R.fail(ApiResultEnum.REQUEST_LIMIT);
        }

        // 执行目标方法（即正常的业务逻辑）
        return joinPoint.proceed();
    }

    /**
     * 获取目标注解
     * 如果方法上有注解就返回方法上的注解配置，否则类上的
     * @param method
     * @param annotationClass
     * @param <A>
     * @return
     */
    public <A extends Annotation> A getTagAnnotation(Method method, Class<A> annotationClass) {
        // 获取方法中是否包含注解
        Annotation methodAnnotate = method.getAnnotation(annotationClass);
        //获取 类中是否包含注解，也就是controller 是否有注解
        Annotation classAnnotate = method.getDeclaringClass().getAnnotation(annotationClass);
        return (A) (methodAnnotate!= null?methodAnnotate:classAnnotate);
    }
}

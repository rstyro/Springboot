package top.lrshuai.limit.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.lrshuai.limit.annotation.RedissonRateLimit;
import top.lrshuai.limit.common.R;
import top.lrshuai.limit.util.AopUtil;

import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
public class RedissonRateLimitAspect {

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 切片-方法级别
     */
    @Around("@annotation(rateLimit)")
    public Object around(ProceedingJoinPoint joinPoint, RedissonRateLimit rateLimit) throws Throwable {
        String key = buildRateLimitKey(joinPoint, rateLimit);
        RRateLimiter rRateLimiter = redissonClient.getRateLimiter(key);
        // 初始化限流器
        rRateLimiter.trySetRate(RateType.OVERALL, rateLimit.rate(), 1, RateIntervalUnit.SECONDS);
        if (!rRateLimiter.tryAcquire(rateLimit.tokens())) {
            log.warn("接口限流触发 - key: {}, 方法: {}", key, joinPoint.getSignature().getName());
            return R.fail(rateLimit.message());
        }
        return joinPoint.proceed();
    }

    /**
     * 构建限流key
     */
    private String buildRateLimitKey(ProceedingJoinPoint joinPoint, RedissonRateLimit rateLimit) {
        String key = rateLimit.key();
        // 如果key为空，使用默认格式
        if (key.isEmpty()) {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            String className = method.getDeclaringClass().getSimpleName();
            String methodName = method.getName();
            return String.format("rate_limit:%s:%s", className, methodName);
        }
        // 如果key包含SpEL表达式，进行解析
        if (key.contains("#")) {
            return AopUtil.parseSpel(key, joinPoint);
        }
        return key;
    }
}

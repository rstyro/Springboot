package top.lrshuai.limit.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.lrshuai.limit.annotation.LeakyBucketLimit;
import top.lrshuai.limit.common.ApiException;
import top.lrshuai.limit.common.ApiResultEnum;
import top.lrshuai.limit.service.LeakyBucketRateLimiter;
import top.lrshuai.limit.util.AopUtil;

import java.lang.reflect.Method;


@Slf4j
@Aspect
@Component
public class LeakyBucketLimitAspect {

    @Autowired
    private LeakyBucketRateLimiter rateLimiter;

    @Around("@annotation(leakyBucketLimit)")
    public Object around(ProceedingJoinPoint joinPoint, LeakyBucketLimit leakyBucketLimit) throws Throwable {
        String key = buildRateLimitKey(joinPoint, leakyBucketLimit);
        int capacity = leakyBucketLimit.capacity();
        int rate = leakyBucketLimit.rate();

        LeakyBucketRateLimiter.BucketStatus bucketStatus = rateLimiter.getBucketStatus(key);
        log.debug("bucket status: key={}, water={},lastLeakTime={},ttl={}",key,
                bucketStatus.getCurrentWater(), bucketStatus.getLastLeakTime(), bucketStatus.getTtl());
        if (!rateLimiter.tryAcquire(key, capacity, rate, 1)) {
            throw new ApiException(ApiResultEnum.REQUEST_LIMIT);
        }

        return joinPoint.proceed();
    }

    /**
     * 构建限流key
     */
    private String buildRateLimitKey(ProceedingJoinPoint joinPoint, LeakyBucketLimit rateLimit) {
        String key = rateLimit.key();

        // 如果key为空，使用默认格式
        if (key.isEmpty()) {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            String className = method.getDeclaringClass().getSimpleName();
            String methodName = method.getName();

            // 尝试获取用户信息
            String userKey = getCurrentUserId();
            return String.format("leaky_bucket:%s:%s:%s", className, methodName, userKey);
        }

        // 如果key包含SpEL表达式，进行解析
        if (key.contains("#")) {
            return AopUtil.parseSpel(key, joinPoint);
        }
        return key;
    }

    private String getCurrentUserId() {
        // 实际项目中从安全上下文获取
        return "user123";
    }
}

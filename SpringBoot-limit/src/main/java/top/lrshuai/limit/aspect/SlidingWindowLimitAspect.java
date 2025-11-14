package top.lrshuai.limit.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.lrshuai.limit.annotation.LeakyBucketLimit;
import top.lrshuai.limit.annotation.SlidingWindowLimit;
import top.lrshuai.limit.common.ApiException;
import top.lrshuai.limit.common.ApiResultEnum;
import top.lrshuai.limit.service.SlidingWindowRateLimiter;
import top.lrshuai.limit.util.AopUtil;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
public class SlidingWindowLimitAspect {

    private final SlidingWindowRateLimiter rateLimiter;

    public SlidingWindowLimitAspect(SlidingWindowRateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
    }

    @Around("@annotation(slidingWindowLimit)")
    public Object around(ProceedingJoinPoint joinPoint, SlidingWindowLimit slidingWindowLimit) throws Throwable {
        String key = buildRateLimitKey(joinPoint, slidingWindowLimit);
        int window = slidingWindowLimit.window();
        int maxCount = slidingWindowLimit.maxCount();

        if (!rateLimiter.tryAcquire(key, window, maxCount, 1)) {
            throw new ApiException(ApiResultEnum.REQUEST_LIMIT);
        }

        return joinPoint.proceed();
    }

    /**
     * 构建限流key
     */
    private String buildRateLimitKey(ProceedingJoinPoint joinPoint, SlidingWindowLimit rateLimit) {
        String key = rateLimit.key();

        // 如果key为空，使用默认格式
        if (key.isEmpty()) {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            String className = method.getDeclaringClass().getSimpleName();
            String methodName = method.getName();

            // 尝试获取用户信息
            String userKey = getCurrentUserId();
            return String.format("sliding_window:%s:%s:%s", className, methodName, userKey);
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

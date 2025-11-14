package top.lrshuai.limit.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.lrshuai.limit.annotation.TokenBucketRateLimit;
import top.lrshuai.limit.common.R;
import top.lrshuai.limit.service.TokenBucketRateLimiter;
import top.lrshuai.limit.util.AopUtil;
import top.lrshuai.limit.util.IpUtil;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
public class TokenBucketRateLimitAspect {

    @Autowired
    private TokenBucketRateLimiter rateLimiter;

    /**
     * 切片-方法级别
     */
    @Around("@annotation(rateLimit)")
    public Object around(ProceedingJoinPoint joinPoint, TokenBucketRateLimit rateLimit) throws Throwable {
        String key = buildRateLimitKey(joinPoint, rateLimit);

        boolean allowed = rateLimiter.tryAcquire(key,rateLimit.rate(),rateLimit.capacity(),rateLimit.tokens());
        if (!allowed) {
            log.warn("接口限流触发 - key: {}, 方法: {}", key, joinPoint.getSignature().getName());
            // 这里可以返回统一的错误结果
            return R.fail(rateLimit.message());
        }
        return joinPoint.proceed();
    }

    /**
     * 构建限流key
     */
    private String buildRateLimitKey(ProceedingJoinPoint joinPoint, TokenBucketRateLimit rateLimit) {
        String key = rateLimit.key();

        // 如果key为空，使用默认格式
        if (key.isEmpty()) {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            String className = method.getDeclaringClass().getSimpleName();
            String methodName = method.getName();

            // 尝试获取用户信息，实现更细粒度的限流
            String userKey = getUserKey();
            return String.format("rate_limit:%s:%s:%s", className, methodName, userKey);
        }

        // 如果key包含SpEL表达式，进行解析
        if (key.contains("#")) {
            return AopUtil.parseSpel(key, joinPoint);
        }

        return key;
    }

    /**
     * 获取用户标识（用户ID或IP）
     */
    private String getUserKey() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes)
                    RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                // 优先使用登录用户ID
                String userId = (String) request.getAttribute("userId");
                if (userId != null) {
                    return "user:" + userId;
                }
                // 降级为使用IP
                return "ip:" + IpUtil.getClientIpAddress(request);
            }
        } catch (Exception e) {
            log.debug("获取用户标识失败", e);
        }
        return "anonymous";
    }

}

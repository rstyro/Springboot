package top.lrshuai.limit.annotation;

import java.lang.annotation.*;

/**
 * 令牌桶限流注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TokenBucketRateLimit {

    /**
     * 限流key，支持SpEL表达式
     */
    String key() default "";

    /**
     * 令牌生成速率 (每秒生成的令牌数)
     */
    double rate() default 10.0;

    /**
     * 桶容量
     */
    int capacity() default 20;

    /**
     * 每次请求消耗的令牌数
     */
    int tokens() default 1;

    /**
     * 限流时的提示信息
     */
    String message() default "请求过于频繁，请稍后再试";
}

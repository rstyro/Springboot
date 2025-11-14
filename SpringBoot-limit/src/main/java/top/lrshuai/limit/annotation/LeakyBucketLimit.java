package top.lrshuai.limit.annotation;

import java.lang.annotation.*;

/**
 * 漏桶限流注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LeakyBucketLimit {

    /**
     * 限流key，支持SpEL表达式
     */
    String key() default "";

    /**
     * 桶的容量（最大请求数）
     */
    int capacity() default 100;

    /**
     * 流出速率（每秒处理多少个请求）
     */
    int rate() default 10;

}
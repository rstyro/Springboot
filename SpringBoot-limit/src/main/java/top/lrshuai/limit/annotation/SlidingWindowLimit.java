package top.lrshuai.limit.annotation;

import java.lang.annotation.*;

/**
 * 滑动时间窗口计数器限流注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SlidingWindowLimit {

    /**
     * 限流key，支持SpEL表达式
     */
    String key() default "";

    /**
     * 时间窗口大小（秒）
     */
    int window() default 60;

    /**
     * 时间窗口内允许的最大请求数
     */
    int maxCount() default 100;

}

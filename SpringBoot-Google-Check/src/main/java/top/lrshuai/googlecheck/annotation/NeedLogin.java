package top.lrshuai.googlecheck.annotation;

import java.lang.annotation.*;

/**
 * 是否需要登录注解
 *
 */
@Documented
@Inherited
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NeedLogin {
    // 是否有效,如果注解在类上，又想要某个方法上不生效，可用这个配置
    boolean isValid() default true;
    //登录注解
    boolean login() default true;
    //google验证注解
    boolean google() default false;
}

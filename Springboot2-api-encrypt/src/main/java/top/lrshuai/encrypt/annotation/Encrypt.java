package top.lrshuai.encrypt.annotation;

import java.lang.annotation.*;

/**
 * 组合注解，接受解密，返回加密
 * @author rstyro
 */
@Documented
@Inherited
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Decode
@Encode
public @interface Encrypt {
}

package top.lrshuai.encrypt.annotation;

import java.lang.annotation.*;

/**
 * 接受参数是否需要解密
 * @author rstyro
 */
@Documented
@Inherited
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Decode {
}

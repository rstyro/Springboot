package top.lrshuai.encrypt.annotation;

import java.lang.annotation.*;

/**
 * 返回数据是否加密
 * @author rstyro
 */
@Documented
@Inherited
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Encode {
}

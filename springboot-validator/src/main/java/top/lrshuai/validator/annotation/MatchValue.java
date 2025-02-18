package top.lrshuai.validator.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 自定义注解
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE,
        ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
// 指定此注解的实现，即:验证器
@Constraint(validatedBy = MatchValidator.class)
public @interface MatchValue {

    /**
     * 固定的值校验
     */
    String[] values() default {};

    /**
     * 校验不通过提示
     */
    String message() default "";

    /**
     * 通过枚举类校验
     */
    Class<? extends Enum>[] enums() default {};

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

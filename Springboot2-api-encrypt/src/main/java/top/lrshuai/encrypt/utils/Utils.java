package top.lrshuai.encrypt.utils;

import org.springframework.core.MethodParameter;

import java.lang.annotation.Annotation;

/**
 * @author rstyro
 * @since 2020-10-20
 */
public class Utils {

    /**
     * 判断方法或类上有没有注解
     * @param method mothod对象
     * @param annotations 注解类数组
     * @param <A> Annotation类型的class
     * @return boolean
     */
    public static  <A extends Annotation> boolean hasMethodAnnotation(MethodParameter method, Class<A>[] annotations){
        if(annotations != null){
            for(Class<A> annotation:annotations){
                if(method.hasMethodAnnotation(annotation) || method.getDeclaringClass().isAnnotationPresent(annotation)){
                    return true;
                }
            }
        }
        return false;
    }
}

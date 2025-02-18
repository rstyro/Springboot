package top.rstyro.poetry.util;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Locale;

@Slf4j
public class LambdaUtil {


    /**
     * 反射得到字段名称
     */
    public static <T, R> String getFieldName(SFunction<T, R> function) {
        String fieldName = null;
        try {
            // 第1步 获取SerializedLambda
            SerializedLambda serializedLambda = getSerializedLambda(function);
            // 第2步 implMethodName 即为Field对应的Getter方法名
            String implMethodName = serializedLambda.getImplMethodName();
            fieldName=methodToProperty(implMethodName);
        } catch (Exception e) {
            log.error("反射获取属性异常，err={}",e.getMessage(),e);
        }
        return fieldName;
    }

    /**
     * 反射得到字段属性
     */
    public static <T, R> Field getField(SFunction<T, R> function) {
        Field field = null;
        String fieldName = null;
        try {
            SerializedLambda serializedLambda = getSerializedLambda(function);
            fieldName=methodToProperty(serializedLambda.getImplMethodName());
            // 获取的Class是字符串，并且包名是“/”分割，需要替换成“.”，才能获取到对应的Class对象
            String declaredClass = serializedLambda.getImplClass().replace("/", ".");
            Class<?> aClass = Class.forName(declaredClass, false, ClassUtils.getDefaultClassLoader());
            //  Spring 中的反射工具类获取Class中定义的Field
            field = ReflectionUtils.findField(aClass, fieldName);
        } catch (Exception e) {
           log.error("反射获取属性异常，err={}",e.getMessage(),e);
        }
        // 第5步 如果没有找到对应的字段应该抛出异常
        if (field != null) {
            return field;
        }
        throw new NoSuchFieldError(fieldName);
    }

    /**
     * 获取SerializedLambda
     */
    @SneakyThrows
    public static <T, R> SerializedLambda getSerializedLambda(SFunction<T, R> function){
        Method method = function.getClass().getDeclaredMethod("writeReplace");
        method.setAccessible(Boolean.TRUE);
        SerializedLambda serializedLambda = (SerializedLambda) method.invoke(function);
        return serializedLambda;
    }


    public static String methodToProperty(String name) {
        if (name.startsWith("is")) {
            name = name.substring(2);
        } else if (name.startsWith("get") || name.startsWith("set")) {
            name = name.substring(3);
        } else {
            throw new RuntimeException("方法名有误，不是以 get set is 开始的");
        }
        if (name.length() == 1 || (name.length() > 1 && !Character.isUpperCase(name.charAt(1)))) {
            name = name.substring(0, 1).toLowerCase(Locale.ENGLISH) + name.substring(1);
        }
        return name;
    }

}
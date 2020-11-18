package top.lrshuai.encrypt.advice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;
import top.lrshuai.encrypt.annotation.Decode;
import top.lrshuai.encrypt.annotation.Encrypt;
import top.lrshuai.encrypt.config.KeyConfig;
import top.lrshuai.encrypt.utils.Utils;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * 请求参数到controller之前的处理
 * @author rstyro
 */
@ControllerAdvice(basePackages = {"top.lrshuai.encrypt.controller"})
public class EncryptRequestAdvice implements RequestBodyAdvice {

    @Autowired
    private KeyConfig keyConfig;

    /**
     * 是否需要解码
     */
    private boolean isDecode;

    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        // 方法或类上有注解
        if (Utils.hasMethodAnnotation(methodParameter,new Class[]{Encrypt.class,Decode.class})) {
            isDecode=true;
            // 这里返回true 才支持
            return true;
        }
        return false;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) throws IOException {
        if(isDecode){
            return new DecodeInputMessage(httpInputMessage, keyConfig);
        }
        return httpInputMessage;
    }

    @Override
    public Object afterBodyRead(Object obj, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        // 这里就是已经读取到body了，obj就是
        return obj;
    }

    @Override
    public Object handleEmptyBody(Object obj, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        // body 为空的时候调用
        return obj;
    }

}

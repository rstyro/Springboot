package top.lrshuai.encrypt.advice;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;
import top.lrshuai.encrypt.dto.TestDto;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * @author rstyro
 */
@ControllerAdvice(basePackages = {"top.lrshuai.encrypt.controller"})
public class EncryptRequestAdvice implements RequestBodyAdvice {
    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        // 这里返回true 才支持
        return true;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) throws IOException {
        // 这里是body 读取之前的处理
        return httpInputMessage;
    }

    @Override
    public Object afterBodyRead(Object obj, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        // 这里就是已经读取到body了，obj就是
        // todo 这里可以解密
        if(obj instanceof TestDto){
            ((TestDto) obj).setInfo("修改信息");
            ((TestDto) obj).setAge(30);
        }
        return obj;
    }

    @Override
    public Object handleEmptyBody(Object obj, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
       // body 为空的时候调用
        return obj;
    }
}

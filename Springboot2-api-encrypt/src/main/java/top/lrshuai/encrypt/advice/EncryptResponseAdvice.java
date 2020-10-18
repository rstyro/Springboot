package top.lrshuai.encrypt.advice;

import com.alibaba.fastjson.JSON;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import top.lrshuai.encrypt.constant.Result;
import top.lrshuai.encryption.AesUtils;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

/**
 * @author rstyro
 */
@ControllerAdvice(basePackages = {"top.lrshuai.encrypt.controller"})
public class EncryptResponseAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        System.out.println("ResponseBodyAdvice====supports");
        // return true 有效
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object obj, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if (obj instanceof Result) {
            try {
                ((Result) obj).put("encode", AesUtils.encodeBase64(JSON.toJSONString(obj), AesUtils.generateSecret(128)));
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return obj;
    }
}

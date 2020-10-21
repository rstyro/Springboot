package top.lrshuai.encrypt.advice;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import top.lrshuai.encrypt.annotation.Encode;
import top.lrshuai.encrypt.annotation.Encrypt;
import top.lrshuai.encrypt.config.KeyConfig;
import top.lrshuai.encrypt.constant.Result;
import top.lrshuai.encrypt.utils.Utils;
import top.lrshuai.encryption.AesUtils;
import top.lrshuai.encryption.RsaUtils;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

/**
 * @author rstyro
 */
@ControllerAdvice(basePackages = {"top.lrshuai.encrypt.controller"})
public class EncryptResponseAdvice implements ResponseBodyAdvice<Object> {

    @Autowired
    private KeyConfig keyConfig;

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        // return true 有效
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object obj, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        // 方法或类上有注解
        if (Utils.hasMethodAnnotation(methodParameter,new Class[]{Encrypt.class, Encode.class})) {
            // 这里假设已经定义好返回的model就是Result
            if (obj instanceof Result) {
                try {
                    // 1、随机aes密钥
                    String randomAesKey = AesUtils.generateSecret(256);
                    // 2、数据体
                    Object data = ((Result) obj).getData();
                    // 3、转json字符串
                    String jsonString = JSON.toJSONString(data);
                    // 4、aes加密
                    String aesEncode = AesUtils.encodeBase64(jsonString, randomAesKey,keyConfig.getAesIv().getBytes(),AesUtils.CIPHER_MODE_CBC_PKCS5PADDING);
                    // 5、放入data
                    ((Result) obj).put(Result.DATA,aesEncode);
                    // 6、aes密钥-使用前端的rsa公钥加密 返回给前端
                    ((Result) obj).put(Result.KEY,RsaUtils.encodeBase64PublicKey(keyConfig.getFrontRsaPublicKey(),randomAesKey));
                    // 7、result 最终转json,然后再次 rsa使用前端的rsa公钥 加密返回
                    return RsaUtils.encodeBase64PublicKey(keyConfig.getFrontRsaPublicKey(), JSON.toJSONString(obj));
                } catch (GeneralSecurityException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return obj;
    }
}

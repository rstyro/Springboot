package top.lrshuai.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConfigConstValue {
    public static String TOKEN;

    @Value("${login.token-key}")
    public void setToken(String token) {
        ConfigConstValue.TOKEN = token;
    }


}

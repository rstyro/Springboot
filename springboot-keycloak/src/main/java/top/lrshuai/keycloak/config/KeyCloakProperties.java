package top.lrshuai.keycloak.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 自定义配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "keycloak")
public class KeyCloakProperties {

    private String baseUri;

    private Map<String,List<AppKey>> realms;

    @Data
    public static class AppKey {
        private String clientId;
        private String clientSecret;
    }
}

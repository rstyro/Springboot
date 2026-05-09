package top.lrshuai.jwt.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private String secret = "your-256-bit-secret-key-for-jwt-signing-must-be-at-least-32-characters-long";
    private String issuer = "jwt-system";
    private String audience = "web-app";
    private Long accessTokenExpire = 7200L;
    private Long refreshTokenExpire = 604800L;
    private String tokenPrefix = "Bearer ";
    private String headerName = "Authorization";
}

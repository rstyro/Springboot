package top.lrshuai.keycloak.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    /**
     * 完全忽略公共路径，不经过 Spring Security 过滤器链
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(
                // OIDC 公开端点（需要忽略，因为不需要认证）
                "/realms/*/protocol/openid-connect/auth",
                "/realms/*/protocol/openid-connect/token",
                "/realms/*/protocol/openid-connect/logout",
                // 其他静态页面或公开接口
                "/public/**",
                "/page/**",
                "/auth/login",
                "/error"
        );
    }

    @Bean
    public Converter<Jwt, AbstractAuthenticationToken> oidcJwtAuthenticationConverter() {
        return new OidcJwtAuthenticationConverter();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 禁用 CSRF（对于无状态 API 推荐）
                .csrf(csrf -> csrf.disable())
                // 配置请求授权
                .authorizeHttpRequests(authorize -> authorize
                        // 由于 WebSecurityCustomizer 已经忽略，这里可以不用再放行，但保留也无妨
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .anyRequest().authenticated()
                )
                // 如果需要支持基于 Session 的登录（例如传统的 OAuth2 登录流程），启用以下配置
                 .oauth2Login(oauth2 -> oauth2
                     .defaultSuccessUrl("/home")
                 )
                // 支持 JWT 认证（Bearer Token）
                .oauth2ResourceServer(oauth2 -> oauth2
//                        .jwt(jwt -> {})
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(oidcJwtAuthenticationConverter()))
                )
                // 会话策略：有状态和无状态共存（但被忽略的路径不受影响）
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                );
        return http.build();
    }
}
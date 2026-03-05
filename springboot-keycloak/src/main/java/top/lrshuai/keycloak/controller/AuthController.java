package top.lrshuai.keycloak.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.lrshuai.keycloak.resp.R;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Keycloak授权控制器
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Value("${server.port}")
    private String serverPort;

    // 登录入口：返回Keycloak登录URL，前端跳转
    @GetMapping("/login")
    public R login() {
        // 获取Keycloak登录页URL（由Spring Security自动生成）
        String authorizationRequestUri = "http://localhost:%s/oauth2/authorization/keycloak".formatted(serverPort);
        // 返回给前端，前端执行 location.href = 该URL
        return R.ok().data(authorizationRequestUri);
    }

    // 获取当前登录用户的令牌信息（需用户已认证）
    @GetMapping("/token")
    public R getToken(@RegisteredOAuth2AuthorizedClient("keycloak") OAuth2AuthorizedClient authorizedClient) {
        if (authorizedClient == null) {
            return R.fail(Map.of("error", "No authorized client found"));
        }
        Map<String, Object> tokenInfo = new HashMap<>();
        tokenInfo.put("access_token", authorizedClient.getAccessToken().getTokenValue());
        tokenInfo.put("refresh_token",
                authorizedClient.getRefreshToken() != null ? authorizedClient.getRefreshToken().getTokenValue() : null);
        tokenInfo.put("expires_at", authorizedClient.getAccessToken().getExpiresAt());
        tokenInfo.put("token_type", "Bearer");
        return R.ok(tokenInfo);
    }

    // 用户信息接口
    @GetMapping("/userinfo")
    public R getUserInfo(@AuthenticationPrincipal OidcUser user) {
        if (user == null) {
            return R.fail(Map.of("error", "User not authenticated"));
        }
        return R.ok(Map.of(
                "sub", user.getSubject(),
                "name", user.getFullName(),
                "preferred_username", user.getPreferredUsername(),
                "email", user.getEmail(),
                "email_verified", user.getEmailVerified(),
                "given_name", user.getGivenName(),
                "family_name", user.getFamilyName()
        ));
    }

    // 注销入口：返回Spring Security注销URL，前端调用
    @GetMapping("/logout")
    public void logout(HttpServletResponse response) throws IOException {
        // Spring Security默认注销URL为 /logout
        response.sendRedirect("/logout");
//        return R.ok().data("/logout");
    }
}
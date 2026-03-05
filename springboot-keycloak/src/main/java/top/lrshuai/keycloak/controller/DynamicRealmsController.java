package top.lrshuai.keycloak.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import top.lrshuai.keycloak.config.KeyCloakProperties;
import top.lrshuai.keycloak.resp.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 多租户多客户端
 */
@RestController
@RequestMapping("/realms")
public class DynamicRealmsController {

    private static final Logger log = LoggerFactory.getLogger(DynamicRealmsController.class);

    @Resource
    private KeyCloakProperties keyCloakProperties;

    @Resource
    private RestTemplate restTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 发起认证请求
     *
     * @param realm        Keycloak realm
     * @param clientId     客户端ID
     * @param redirect_uri 前端回调地址（必须与Keycloak客户端配置一致）
     * @param scope        权限范围：openid profile email
     * @param state        防CSRF状态值（前端生成并保存）
     * @param nonce        随机数
     * @param access_type  访问类型（online/offline）
     * @param response     HttpServletResponse
     */
    @GetMapping("/{realm}/protocol/openid-connect/auth")
    public void auth(
            @PathVariable String realm,
            @RequestParam String clientId,
            @RequestParam String redirect_uri,
            @RequestParam String scope,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String nonce,
            @RequestParam(required = false) String access_type,
            HttpServletResponse response) throws IOException {

        // 验证 clientId 是否存在
        boolean clientValid = keyCloakProperties.getRealms()
                .getOrDefault(realm,new ArrayList<>())
                .stream()
                .anyMatch(k -> k.getClientId().equals(clientId));
//        boolean clientValid = keyCloakProperties.getAppKeys().stream()
//                .anyMatch(k -> k.getClientId().equals(clientId));
        if (!clientValid) {
            log.warn("Invalid client_id: {}", clientId);
            sendErrorRedirect(response, redirect_uri, "unauthorized_client", "Invalid client_id", state);
            return;
        }

        if (state == null) {
            state = UUID.randomUUID().toString(); // 建议前端生成，此处仅为后备
        }

        String issuer = keyCloakProperties.getBaseUri() + "/realms/" + realm;
        String keycloakAuthUrl = UriComponentsBuilder
                .fromHttpUrl(issuer + "/protocol/openid-connect/auth")
                .queryParam("response_type", "code")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirect_uri)
                .queryParam("scope", scope)
                .queryParam("state", state)
                .queryParam("nonce", nonce != null ? nonce : UUID.randomUUID().toString())
                .queryParam("access_type", access_type != null ? access_type : "online")
                .build()
                .toUriString();

        log.debug("Redirecting to Keycloak auth URL: {}", keycloakAuthUrl);
        response.sendRedirect(keycloakAuthUrl);
    }

    private void sendErrorRedirect(HttpServletResponse response, String redirectUri,
                                   String error, String errorDescription, String state) throws IOException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(redirectUri)
                .queryParam("error", error)
                .queryParam("error_description", errorDescription);
        if (state != null) {
            builder.queryParam("state", state);
        }
        response.sendRedirect(builder.build().toUriString());
    }


    /**
     * 令牌获取
     * @param realm Keycloak realm
     * @param grant_type 授权类型：authorization_code=授权码模式
     * @param code 授权码
     * @param redirect_uri 回调地址
     * @param clientId 客户端ID
     * @param code_verifier 授权码 校验
     * @return
     */
    @PostMapping(value = "/{realm}/protocol/openid-connect/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public R<?> token(
            @PathVariable String realm,
            @RequestParam String grant_type,
            @RequestParam String code,
            @RequestParam String redirect_uri,
            @RequestParam String clientId,
            @RequestParam(required = false) String code_verifier) {

        if (!"authorization_code".equals(grant_type)) {
            return buildError("unsupported_grant_type", "Only authorization_code is supported");
        }
        KeyCloakProperties.AppKey appKey = keyCloakProperties.getRealms()
                .getOrDefault(realm,new ArrayList<>())
                .stream()
                .filter(k -> k.getClientId().equals(clientId))
                .findFirst()
                .orElse(null);

        if (appKey == null) {
            return buildError("invalid_client", "Client not found");
        }

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("client_secret", appKey.getClientSecret());
        params.add("code", code);
        params.add("redirect_uri", redirect_uri);
        if (code_verifier != null) {
            params.add("code_verifier", code_verifier);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        String issuer = keyCloakProperties.getBaseUri() + "/realms/" + realm;
        String keycloakTokenUri = issuer + "/protocol/openid-connect/token";

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    keycloakTokenUri,
                    HttpMethod.POST,
                    request,
                    Map.class
            );
            return R.ok(response.getBody());
        } catch (HttpClientErrorException e) {
            log.error("Token exchange failed: {}", e.getResponseBodyAsString(), e);
            Map<String, Object> errorBody = extractErrorBody(e);
            return R.fail(errorBody);
        } catch (Exception e) {
            log.error("Unexpected error during token exchange", e);
            return buildError("server_error", e.getMessage());
        }
    }

    private Map<String, Object> extractErrorBody(HttpClientErrorException e) {
        try {
            return objectMapper.readValue(e.getResponseBodyAsString(), Map.class);
        } catch (Exception ex) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "invalid_request");
            error.put("error_description", e.getMessage());
            return error;
        }
    }

    private R<?> buildError(String error, String description) {
        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("error", error);
        errorBody.put("error_description", description);
        return R.fail(errorBody);
    }

    /**
     * 用户信息获取
     */
    @GetMapping("/{realm}/protocol/openid-connect/userinfo")
    public R<?> userinfo(@AuthenticationPrincipal Jwt user,
                         @RequestHeader("Authorization") String authHeader,@PathVariable String realm) {
        log.info("Authorization header: {}",authHeader);
         if (user != null) {
            System.out.println("===JwtAuthenticationToken===");
            return R.ok(user);
        }
        System.out.println("===Keycloak-API===");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return R.fail(401, "Access token missing or invalid");
        }
        // 提取 access token
        String accessToken = authHeader.substring(7);

        // 调用 Keycloak 的 userinfo 端点
        String issuer = keyCloakProperties.getBaseUri() + "/realms/" + realm;
        String userinfoUri = issuer + "/protocol/openid-connect/userinfo";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);
        HttpEntity<?> request = new HttpEntity<>(headers);
        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    userinfoUri,
                    HttpMethod.GET,
                    request,
                    Map.class
            );
            return R.ok(response.getBody());
        } catch (HttpClientErrorException e) {
            log.error("Userinfo request failed: {}", e.getResponseBodyAsString(), e);
            Map<String, Object> errorBody = extractErrorBody(e);
            return R.fail(errorBody);
        } catch (Exception e) {
            log.error("Unexpected error during userinfo request", e);
            return buildError("server_error", e.getMessage());
        }
    }

    /**
     * RP-initiated 登出
     */
    @GetMapping("/{realm}/protocol/openid-connect/logout")
    public void rpInitiatedLogout(
            @PathVariable String realm,
            @RequestParam(required = false) String id_token_hint,
            @RequestParam(required = false) String post_logout_redirect_uri,
            @RequestParam(required = false) String state,
            HttpServletResponse response) throws IOException {

        String issuer = keyCloakProperties.getBaseUri() + "/realms/" + realm;
        String logoutUrl = issuer + "/protocol/openid-connect/logout";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(logoutUrl);
        if (id_token_hint != null) {
            builder.queryParam("id_token_hint", id_token_hint);
        }
        if (post_logout_redirect_uri != null) {
            builder.queryParam("post_logout_redirect_uri", post_logout_redirect_uri);
        }
        if (state != null) {
            builder.queryParam("state", state);
        }
        response.sendRedirect(builder.build().toUriString());
    }
}
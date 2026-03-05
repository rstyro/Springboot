package top.lrshuai.keycloak.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;

/**
 * 自定义 JWT 认证转换器
 */
@Slf4j
public class OidcJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        log.info("Converting JWT to OidcUser");
        // 提取 claims
        Map<String, Object> claims = jwt.getClaims();

        // 构造 OidcIdToken (使用 JWT 本身作为 ID Token 的值)
        OidcIdToken idToken = new OidcIdToken(jwt.getTokenValue(), jwt.getIssuedAt(), jwt.getExpiresAt(), claims);

        // 构造 OidcUserInfo (从 claims 中提取用户信息)
        OidcUserInfo userInfo = new OidcUserInfo(claims);

        // 提取权限 (例如从 scope 或 realm_access 中获取)
        Collection<GrantedAuthority> authorities = extractAuthorities(jwt);

        // 创建 OidcUser
        OidcUser oidcUser = new DefaultOidcUser(authorities, idToken, userInfo);
        log.info("Created OidcUser: {}", oidcUser.getClaims());
        // 返回 JwtAuthenticationToken，principal 为 OidcUser
        return new JwtAuthenticationToken(jwt, authorities, oidcUser.getName());
    }

    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        // 示例：从 scope 声明中提取
        String scope = jwt.getClaim("scope");
        if (scope != null) {
            Arrays.stream(scope.split(" "))
                    .forEach(s -> authorities.add(new SimpleGrantedAuthority("SCOPE_" + s)));
        }
        // 如果需要从 Keycloak 的 realm_access 中提取角色，可以在此添加逻辑
        return authorities;
    }
}

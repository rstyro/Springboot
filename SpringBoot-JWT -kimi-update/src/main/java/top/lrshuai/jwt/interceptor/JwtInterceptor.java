package top.lrshuai.jwt.interceptor;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import top.lrshuai.jwt.common.ResultCode;
import top.lrshuai.jwt.exception.BusinessException;
import top.lrshuai.jwt.util.JwtUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = extractTokenFromRequest(request);

        if (token == null) {
            throw new BusinessException(ResultCode.UNAUTHORIZED, "未提供认证Token");
        }

        if (isTokenBlacklisted(token)) {
            throw new BusinessException(ResultCode.TOKEN_REVOKED, "Token已被吊销");
        }

        try {
            Claims claims = jwtUtils.parseToken(token);
            Long userId = claims.get("userId", Long.class);
            String username = claims.get("username", String.class);

            request.setAttribute("userId", userId);
            request.setAttribute("username", username);
            request.setAttribute("claims", claims);

            return true;
        } catch (Exception e) {
            log.warn("Token验证失败: {}", e.getMessage());
            throw new BusinessException(ResultCode.TOKEN_INVALID, "Token无效或已过期");
        }
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private boolean isTokenBlacklisted(String token) {
        Boolean isMember = redisTemplate.opsForSet().isMember("token:blacklist", token);
        return Boolean.TRUE.equals(isMember);
    }
}

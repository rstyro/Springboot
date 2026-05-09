package top.lrshuai.jwt.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.lrshuai.jwt.config.JwtProperties;
import top.lrshuai.jwt.dto.TokenParseResponse;
import top.lrshuai.jwt.dto.TokenResponse;
import top.lrshuai.jwt.entity.User;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
public class JwtUtils {

    @Autowired
    private JwtProperties jwtProperties;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    public TokenResponse generateTokenPair(User user) {
        String accessToken = generateAccessToken(user);
        String refreshToken = generateRefreshToken(user);

        LocalDateTime accessExpireTime = LocalDateTime.now().plusSeconds(jwtProperties.getAccessTokenExpire());
        LocalDateTime refreshExpireTime = LocalDateTime.now().plusSeconds(jwtProperties.getRefreshTokenExpire());

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtProperties.getAccessTokenExpire())
                .expireTime(accessExpireTime)
                .refreshExpireTime(refreshExpireTime)
                .build();
    }

    public String generateAccessToken(User user) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + jwtProperties.getAccessTokenExpire() * 1000);

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getUserId());
        claims.put("username", user.getUsername());
        claims.put("roles", user.getRoles());
        claims.put("permissions", user.getPermissions());
        claims.put("type", "access");

        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setIssuer(jwtProperties.getIssuer())
                .setAudience(jwtProperties.getAudience())
                .setSubject(user.getUserId().toString())
                .setIssuedAt(now)
                .setExpiration(expiration)
                .setNotBefore(now)
                .addClaims(claims)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(User user) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + jwtProperties.getRefreshTokenExpire() * 1000);

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getUserId());
        claims.put("type", "refresh");

        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setIssuer(jwtProperties.getIssuer())
                .setAudience(jwtProperties.getAudience())
                .setSubject(user.getUserId().toString())
                .setIssuedAt(now)
                .setExpiration(expiration)
                .setNotBefore(now)
                .addClaims(claims)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("Token已过期: {}", e.getMessage());
            return false;
        } catch (UnsupportedJwtException e) {
            log.warn("不支持的Token: {}", e.getMessage());
            return false;
        } catch (MalformedJwtException e) {
            log.warn("Token格式错误: {}", e.getMessage());
            return false;
        } catch (SignatureException e) {
            log.warn("Token签名验证失败: {}", e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            log.warn("Token为空或非法: {}", e.getMessage());
            return false;
        }
    }

    public TokenParseResponse parseTokenDetails(String token) {
        try {
            Claims claims = parseToken(token);
            return TokenParseResponse.builder()
                    .id(claims.getId())
                    .issuer(claims.getIssuer())
                    .subject(claims.getSubject())
                    .audience(claims.getAudience())
                    .issuedAt(convertToLocalDateTime(claims.getIssuedAt()))
                    .expiration(convertToLocalDateTime(claims.getExpiration()))
                    .notBefore(convertToLocalDateTime(claims.getNotBefore()))
                    .userId(claims.get("userId", Long.class))
                    .username(claims.get("username", String.class))
                    .claims(new HashMap<>(claims))
                    .valid(true)
                    .message("Token验证通过")
                    .build();
        } catch (ExpiredJwtException e) {
            return TokenParseResponse.builder()
                    .valid(false)
                    .message("Token已过期: " + e.getMessage())
                    .build();
        } catch (Exception e) {
            return TokenParseResponse.builder()
                    .valid(false)
                    .message("Token验证失败: " + e.getMessage())
                    .build();
        }
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.get("userId", Long.class);
    }

    public String getUsernameFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.get("username", String.class);
    }

    public boolean isTokenExpired(String token) {
        try {
            Claims claims = parseToken(token);
            return claims.getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    public Date getExpirationDateFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.getExpiration();
    }

    private LocalDateTime convertToLocalDateTime(Date date) {
        if (date == null) return null;
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}

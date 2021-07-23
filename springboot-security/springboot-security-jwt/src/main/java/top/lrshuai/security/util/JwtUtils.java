package top.lrshuai.security.util;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import top.lrshuai.security.config.security.SecurityUser;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.util.*;

@Slf4j
@Component
public class JwtUtils {

    /**
     * jwt 密钥
     */
    private static String JWT_SECRET_KEY="rstyro2dcad64e9cbd542c5584f1103110e6ce2dcad64e9cbd542c5584rstyro";

    /**
     * 过期时间 毫秒
     */
    private static Long EXPIRE_TIME=300000l;

    /**
     * 主题订阅
     */
    private static String SUBJECT = "token";

    private static String ROLE_KEY = "roles";
    private static String USERNAME_KEY = "username";

    @Value("${jwt.secret}")
    public void setJwtSecretKey(String jwtSecretKey) {
        JWT_SECRET_KEY = jwtSecretKey;
    }

    @Value("${jwt.expireTime}")
    public void setExpiration(Long expireTime) {
        EXPIRE_TIME = expireTime * 60 * 1000;
    }

    @Value("${jwt.subject}")
    public void setSubject(String subject) {
        SUBJECT = subject;
    }

    /**
     * 获取自定义密钥
     * @return
     */
    private static SecretKey getJwtSecretKey() {
        if(!StringUtils.hasLength(JWT_SECRET_KEY)) {
            throw new RuntimeException("jjwt配置的密钥不能为空");
        }
        // 根据给定的字节数组使用HmacSHA256加密算法构造一个密钥
        SecretKey key = new SecretKeySpec(JWT_SECRET_KEY.getBytes(), "HmacSHA256");
        return key;
    }


    /**
     * 生成 jwt token
     */
    public static String generateKey(Map<String, Object> claims)  {
        Date now = new Date();
        Date expirationDate = new Date(System.currentTimeMillis()+EXPIRE_TIME);
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setId(UUID.randomUUID().toString())
                .setSubject(SUBJECT)
                .setExpiration(expirationDate)//过期时间
                .signWith(getJwtSecretKey(),SignatureAlgorithm.HS256)
                .compact();
        log.info("token===" + token);
        return token;
    }

    /**
     * 生成token
     */
    public static String generateKey(SecurityUser user)  {
        Map<String, Object> claims = new HashMap<>();
        claims.put(USERNAME_KEY,user.getUsername());
        claims.put(ROLE_KEY,user.getAuthorities().toString());
        Date now = new Date();
        Date expirationDate = new Date(Instant.now().toEpochMilli()+EXPIRE_TIME);
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setId(UUID.randomUUID().toString())
                .setSubject(SUBJECT)
                .setExpiration(expirationDate)//过期时间
                .signWith(getJwtSecretKey(),SignatureAlgorithm.HS256)
                .compact();
        log.info("token===" + token);
        return token;
    }

    /**
     * 解析token
     */
    public static SecurityUser getUserInfoByToken(String token)  {
        Set<GrantedAuthority> authoritiesSet = new HashSet();
        Optional<Claims> claimsOptional = parseToken(token);
        if(claimsOptional.isPresent()){
            Claims claims = claimsOptional.get();
            String roles = claims.get(ROLE_KEY, String.class);
            if(StringUtils.hasLength(roles)){
                roles = roles.substring(1, roles.length() - 1);
                String[] strArray = roles.split(",");
                Arrays.stream(strArray).forEach(role->{
                    GrantedAuthority authority = new SimpleGrantedAuthority(role.trim());
                    authoritiesSet.add(authority);
                });
            }
            return new SecurityUser().setUsername(claims.get(USERNAME_KEY,String.class)).setAuthorities(authoritiesSet);
        }
        return null;
    }

    /**
     * 解析jwt
     * @param jwsToken jws
     */
    public static Optional<Claims> parseToken(String jwsToken){
        try{
            Jws<Claims> parse = Jwts.parserBuilder()
                    .requireSubject(SUBJECT)
                    .setSigningKey(getJwtSecretKey())
                    .build()
                    .parseClaimsJws(jwsToken);
            return Optional.of(parse.getBody());
        }catch (JwtException  e){
            log.error("解析jwt失败：",e);
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return Optional.empty();
    }


    /**
     * token 是否过期
     * @param jwsToken token
     * @return boolean
     */
    public static boolean isExpiration(String jwsToken){
        Optional<Claims> claims = parseToken(jwsToken);
        if(claims.isPresent()){
            return claims.get().getExpiration().before(new Date());
        }
        return true;
    }

    public static void main(String[] args) throws InterruptedException {
        String line = "[user:list, user:list:edit, user:list:del, ROLE_test, user:list:add, user:list:query]";
        System.out.println(line.substring(1,line.length()-1));
        String[] split = line.replace("[","").replace("]","").split(",");
        System.out.println(split.length);
        System.out.println(split[0]);

    }

}

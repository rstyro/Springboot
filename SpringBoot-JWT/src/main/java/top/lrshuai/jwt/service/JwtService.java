package top.lrshuai.jwt.service;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Service;
import top.lrshuai.jwt.common.ApiException;
import top.lrshuai.jwt.common.ApiResultEnum;
import top.lrshuai.jwt.dto.TokenGenerateRequest;
import top.lrshuai.jwt.dto.TokenResponse;
import top.lrshuai.jwt.dto.TokenVerifyRequest;
import top.lrshuai.jwt.entity.RSA256Key;
import top.lrshuai.jwt.entity.User;
import top.lrshuai.jwt.util.CreateSecrteKey;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {

    private static final String SECRET = "rstyro";
    private static final String ISSUER = "rstyro";
    private static final String SUBJECT = "test";
    private static final long DEFAULT_EXPIRE_SECONDS = 7200L;
    private static final long SHORT_LIVED_EXPIRE_SECONDS = 20L;
    private static final ThreadLocal<SimpleDateFormat> DATE_FORMAT = ThreadLocal.withInitial(
            () -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    );

    public TokenResponse generateToken(TokenGenerateRequest request) throws Exception {
        Algorithm algorithm = getAlgorithm(request.getAlgorithm());
        User userData = buildUserData(request);
        long expireSeconds = getExpireSeconds(request);
        Date expireDate = calculateExpireDate(expireSeconds);
        
        String token = JWT.create()
                .withIssuer(ISSUER)
                .withSubject(SUBJECT)
                .withIssuedAt(new Date())
                .withExpiresAt(expireDate)
                .withClaim("data", JSON.toJSONString(userData))
                .withNotBefore(new Date())
                .withJWTId(UUID.randomUUID().toString())
                .sign(algorithm);

        return TokenResponse.builder()
                .token(token)
                .algorithm(request.getAlgorithm().toUpperCase())
                .expireTime(expireDate.getTime())
                .expireTimeStr(DATE_FORMAT.get().format(expireDate))
                .build();
    }

    public User verifyToken(TokenVerifyRequest request) throws Exception {
        Algorithm algorithm = getVerifyAlgorithm(request.getAlgorithm());
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(ISSUER)
                .build();
        
        try {
            DecodedJWT decodedJWT = verifier.verify(request.getToken());
            String dataString = decodedJWT.getClaim("data").asString();
            return JSON.parseObject(dataString, User.class);
        } catch (TokenExpiredException ex) {
            throw new ApiException(ApiResultEnum.TOKEN_EXPIRED);
        } catch (JWTVerificationException ex) {
            throw new ApiException(ApiResultEnum.SIGN_VERIFI_ERROR);
        }
    }

    private Algorithm getAlgorithm(String algorithmType) throws Exception {
        if (algorithmType == null) {
            throw new ApiException(ApiResultEnum.ALGORITHM_CAN_NOT_NULL);
        }
        
        String upperAlg = algorithmType.toUpperCase();
        switch (upperAlg) {
            case "HS256":
                return Algorithm.HMAC256(SECRET);
            case "RS256":
                RSA256Key rsa256Key = CreateSecrteKey.getRSA256Key();
                return Algorithm.RSA256(rsa256Key.getPublicKey(), rsa256Key.getPrivateKey());
            default:
                throw new ApiException(ApiResultEnum.ALGORITHM_NOT_SUPPORT);
        }
    }

    private Algorithm getVerifyAlgorithm(String algorithmType) throws Exception {
        if (algorithmType == null) {
            throw new ApiException(ApiResultEnum.ALGORITHM_CAN_NOT_NULL);
        }
        
        String upperAlg = algorithmType.toUpperCase();
        switch (upperAlg) {
            case "HS256":
                return Algorithm.HMAC256(SECRET);
            case "RS256":
                RSA256Key rsa256Key = CreateSecrteKey.getRSA256Key();
                return Algorithm.RSA256(rsa256Key.getPublicKey(), null);
            default:
                throw new ApiException(ApiResultEnum.ALGORITHM_NOT_SUPPORT);
        }
    }

    private User buildUserData(TokenGenerateRequest request) {
        if (request.getUserId() != null || request.getUsername() != null) {
            User user = new User();
            user.setUserId(request.getUserId() != null ? request.getUserId() : 1L);
            user.setUsername(request.getUsername() != null ? request.getUsername() : "admin");
            user.setAge(24);
            user.setSex(1);
            return user;
        }
        return User.getAuther();
    }

    private long getExpireSeconds(TokenGenerateRequest request) {
        if (Boolean.TRUE.equals(request.getShortLived())) {
            return SHORT_LIVED_EXPIRE_SECONDS;
        }
        return request.getExpireSeconds() != null ? request.getExpireSeconds() : DEFAULT_EXPIRE_SECONDS;
    }

    private Date calculateExpireDate(long expireSeconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, (int) expireSeconds);
        return calendar.getTime();
    }
}

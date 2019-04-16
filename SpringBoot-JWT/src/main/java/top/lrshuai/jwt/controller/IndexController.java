package top.lrshuai.jwt.controller;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import top.lrshuai.jwt.common.ApiException;
import top.lrshuai.jwt.common.ApiResultEnum;
import top.lrshuai.jwt.common.Result;
import top.lrshuai.jwt.dto.JWTDTO;
import top.lrshuai.jwt.entity.RSA256Key;
import top.lrshuai.jwt.entity.User;
import top.lrshuai.jwt.util.CreateSecrteKey;
import top.lrshuai.jwt.util.DateUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Api(tags = "测试控制层")
@RestController
public class IndexController {

    // 加密密钥
    private final static String secret = "rstyro";


    @ApiOperation("获取Token 通过HS256加密")
    @GetMapping("/getTokenByHS256")
    public Result generTokenByHS256() throws Exception{
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return Result.ok(createToken(algorithm,User.getAuther()));
    }

    @ApiOperation("获取Token 通过RS256加密")
    @GetMapping("/getTokenByRS256")
    public Result generTokenByRS256() throws Exception{
        RSA256Key rsa256Key = CreateSecrteKey.getRSA256Key();
        Algorithm algorithm = Algorithm.RSA256(rsa256Key.getPublicKey(), rsa256Key.getPrivateKey());
        // 返回token
        return Result.ok(createToken(algorithm,User.getAuther()));
    }


    @ApiOperation("获取token ,但是 10 秒内，有效")
    @GetMapping("/getTokenExpire")
    public Result getTokenByRS256AndTimer() throws Exception{
        RSA256Key rsa256Key = CreateSecrteKey.getRSA256Key();
        Algorithm algorithm = Algorithm.RSA256(rsa256Key.getPublicKey(), rsa256Key.getPrivateKey());
        String token =JWT.create()
                .withIssuer("rstyro")   //发布者
                .withSubject("test")    //主题
                .withIssuedAt(new Date())   // 生成签名的时间
                .withExpiresAt(DateUtils.offset(new Date(),20, Calendar.SECOND))    // 生成签名的有效期,20秒
                .withClaim("data", JSON.toJSONString(User.getAuther())) //存数据
                .sign(algorithm);
        // 返回token
        return Result.ok(token);
    }

    /**
     * 生成token
     * @param algorithm
     * @param data
     * @return
     */
    public String createToken(Algorithm algorithm,Object data){
        String[] audience  = {"app","web"};
        return JWT.create()
                .withIssuer("rstyro")   //发布者
                .withSubject("test")    //主题
                .withAudience(audience)     //观众，相当于接受者
                .withIssuedAt(new Date())   // 生成签名的时间
                .withExpiresAt(DateUtils.offset(new Date(),2, Calendar.HOUR_OF_DAY))    // 生成签名的有效期,分钟
                .withClaim("data", JSON.toJSONString(data)) //存数据
                .withNotBefore(new Date())  //生效时间
                .withJWTId(UUID.randomUUID().toString())    //编号
                .sign(algorithm);
    }

    /**
     * 通过token 返回数据
     * @param jwtdto
     * @return
     * @throws Exception
     */
    @ApiOperation("通过token 获取数据")
    @PostMapping("/getDataByToken")
    public Result getDataByToken(JWTDTO jwtdto) throws Exception{
        Algorithm algorithm =null;
        DecodedJWT verify =null;
        if(StringUtils.isEmpty(jwtdto.getAlg())){
            throw new ApiException(ApiResultEnum.ALGORITHM_CAN_NOT_NULL);
        }
        if("rs256".equalsIgnoreCase(jwtdto.getAlg())){
            algorithm = Algorithm.RSA256(CreateSecrteKey.getRSA256Key().getPublicKey(), null);
        }else {
            algorithm = Algorithm.HMAC256(secret);
        }
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("rstyro")
                .build();
        try {
            verify = verifier.verify(jwtdto.getToken());
        }catch (TokenExpiredException ex){
            throw new ApiException(ApiResultEnum.TOKEN_EXPIRED);
        }catch (JWTVerificationException ex){
            throw new ApiException(ApiResultEnum.SIGN_VERIFI_ERROR );
        }
        String dataString = verify.getClaim("data").asString();
        return Result.ok(JSON.parseObject(dataString,User.class));
    }
}

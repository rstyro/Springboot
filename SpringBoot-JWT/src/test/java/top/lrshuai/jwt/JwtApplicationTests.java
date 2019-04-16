package top.lrshuai.jwt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.lrshuai.jwt.entity.RSA256Key;
import top.lrshuai.jwt.entity.User;
import top.lrshuai.jwt.util.CreateSecrteKey;
import top.lrshuai.jwt.util.DateUtils;

import java.util.Calendar;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JwtApplicationTests {


    @Test
    public void test() {
        Algorithm algorithm = Algorithm.HMAC256("rstyro");
        String token = JWT.create()
                .withIssuer("rstyro")
                .sign(algorithm);
        System.out.println("token="+token);
    }

    @Test
    public void testRS256() throws Exception {
        String[] audience  = {"app","web"};
        RSA256Key rsa256Key = CreateSecrteKey.getRSA256Key();
        Algorithm algorithm = Algorithm.RSA256(rsa256Key.getPublicKey(), rsa256Key.getPrivateKey());
        User user = new User();
        user.setUserId(1l);
        user.setAge(24);
        user.setSex(1);
        user.setUsername("rstyro");
        String token = JWT.create()
                .withIssuer("rstyro")   //发布者
                .withSubject("test")    //主题
                .withAudience(audience)     //观众，相当于接受者
                .withIssuedAt(new Date())   // 生成签名的时间
                .withExpiresAt(DateUtils.offset(new Date(),1, Calendar.SECOND))    // 生成签名的有效期
                .withClaim("data", JSON.toJSONString(user)) //存数据
                .withClaim("other", "this is a message") //存数据
                .withClaim("file", "这是一个文件，存数据用这个claim,如果有多的数据，用 withArrayClaim() 方法") //存数据
                .sign(algorithm);
        System.out.println("token="+token);
        System.out.println("public="+CreateSecrteKey.getPublicKey(rsa256Key));
        System.out.println("private="+CreateSecrteKey.getPrivateKey(rsa256Key));

        Thread.sleep(1000*3);

        Algorithm veifierAlgorithm = Algorithm.RSA256(rsa256Key.getPublicKey(), null);
        JWTVerifier verifier = JWT.require(veifierAlgorithm)
                .withIssuer("rstyro")
                .build(); //Reusable verifier instance
        DecodedJWT jwt = verifier.verify(token);
        Claim data = jwt.getClaim("data");
        String userstring = data.asString();
        JSONObject object = JSON.parseObject(userstring);
        User user1 = JSON.parseObject(userstring, User.class);
        System.out.println("user1="+user1);

    }

}

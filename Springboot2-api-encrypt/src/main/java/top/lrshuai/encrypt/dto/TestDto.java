package top.lrshuai.encrypt.dto;

import lombok.Data;
import lombok.ToString;
import org.apache.commons.codec.binary.Base64;
import top.lrshuai.encryption.AesUtils;
import top.lrshuai.encryption.RsaUtils;

/**
 * @author rstyro
 */
@Data
@ToString
public class TestDto {
    private String userId;
    private String userName;
    private Integer age;
    private String info;

    public static void main(String[] args) throws Exception {
        String rsaPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCJvCDW9GIBsiv9ma9r2btffIxQQHB98Pl1S2RV2PrQsK1O2yFSUf8P43l5EfAh+jiEn/k5egKEoeMRLdDZkt5afNgPYbNjiRFJP8NZTw4f3Yxp91+d04GGkeFcj59QIn/rqqHo2JLOESNae8IC1tKKQTqkwVIjLRwTIDcVmsq9NwIDAQAB";
        String rsaPrivateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIm8INb0YgGyK/2Zr2vZu198jFBAcH3w+XVLZFXY+tCwrU7bIVJR/w/jeXkR8CH6OISf+Tl6AoSh4xEt0NmS3lp82A9hs2OJEUk/w1lPDh/djGn3X53TgYaR4VyPn1Aif+uqoejYks4RI1p7wgLW0opBOqTBUiMtHBMgNxWayr03AgMBAAECgYBKaAMlnGfFmscA/SEzFjCO6O2z/NvIbYGVx+CwL8NvMcKuMtrRadJsduqMaRBcipw1qWYtkqgBlqLgCOwmXZ0YYfeIip63ucQ0KLCJOKcPyP8TIUbiHr/RfYLip7bHTycgav+icYWxTx+NWQVwI4BMpexuwjnilRKFEur0s4BoAQJBAMSt225txa0xZVh+UOjw9GuYlf3aHLiPkQ4OKb7MTi0ufdfJ+U5oJFqEvVQsYVdWRMY8OWYp9UtvG13CFBpMkjcCQQCzRwV0ekTbwmdLoOEcypcvFw5AqIF/lYwjRr7uJGA79cYyoxUaSlem79siEaVAUFX6xOLRpqen9zovnQJwSa0BAkAztHUEce1O27aF7ic9JeNLygBcjROR3YRHyqdk1ncS368LpLhayXwNI+pWD5jDihVoe/qnBg7Ldvkdy+DXDRw/AkAFgmM6hx9imYDPJCyG0/r9aXn4prUEFLZvxxbK/rcuYIksuTJG0o4LBUf4rg4kAdQCltZZlwOD9+cD25FdngYBAkEAmHLpYTqgjtITv7VyJ92+4MTE5f+LxpHGHPjgz4XDUHq3XfxXbZijRBFe9cBdbu3nfJxZRpmn6/ofrLb2HdBfMQ==";
        String content = "UA1TAzo7MDZQlbM2lR7857XAZKlp7X5jYAeyFfG3+qTSuc/w/KZoUQWqku8KhFkB82cle8pyCqPHNPleYJVcy0mEFgf1ztHEHYymRm3QIPMfy8Ie8vZY8FN2rpc5zH6bdrzoSi0VxuGob/BTlXet+/eRHtZJtsWwcRNBKA2LaoU=";
        String text="abccccc";
        System.out.println("htlm="+RsaUtils.decodeBase64ByPrivate(rsaPrivateKey, content));
        String encodeText = RsaUtils.encodeBase64PrivateKey(rsaPrivateKey, text);
        System.out.println("encodeText="+encodeText);
        System.out.println("decodeTest="+RsaUtils.decodeBase64ByPublicKey(rsaPublicKey, encodeText));
        AesUtils.generateSecret(128);
    }
}

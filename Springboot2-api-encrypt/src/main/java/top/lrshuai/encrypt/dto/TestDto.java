package top.lrshuai.encrypt.dto;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import top.lrshuai.encryption.RsaUtils;

/**
 * @author rstyro
 */
@Data
@ToString
@Accessors(chain = true)
public class TestDto {
    private String userId;
    private String userName;
    private Integer age;
    private String info;

    public static void main(String[] args) throws Exception {
        String rsaPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCJvCDW9GIBsiv9ma9r2btffIxQQHB98Pl1S2RV2PrQsK1O2yFSUf8P43l5EfAh+jiEn/k5egKEoeMRLdDZkt5afNgPYbNjiRFJP8NZTw4f3Yxp91+d04GGkeFcj59QIn/rqqHo2JLOESNae8IC1tKKQTqkwVIjLRwTIDcVmsq9NwIDAQAB";
        String rsaPrivateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIm8INb0YgGyK/2Zr2vZu198jFBAcH3w+XVLZFXY+tCwrU7bIVJR/w/jeXkR8CH6OISf+Tl6AoSh4xEt0NmS3lp82A9hs2OJEUk/w1lPDh/djGn3X53TgYaR4VyPn1Aif+uqoejYks4RI1p7wgLW0opBOqTBUiMtHBMgNxWayr03AgMBAAECgYBKaAMlnGfFmscA/SEzFjCO6O2z/NvIbYGVx+CwL8NvMcKuMtrRadJsduqMaRBcipw1qWYtkqgBlqLgCOwmXZ0YYfeIip63ucQ0KLCJOKcPyP8TIUbiHr/RfYLip7bHTycgav+icYWxTx+NWQVwI4BMpexuwjnilRKFEur0s4BoAQJBAMSt225txa0xZVh+UOjw9GuYlf3aHLiPkQ4OKb7MTi0ufdfJ+U5oJFqEvVQsYVdWRMY8OWYp9UtvG13CFBpMkjcCQQCzRwV0ekTbwmdLoOEcypcvFw5AqIF/lYwjRr7uJGA79cYyoxUaSlem79siEaVAUFX6xOLRpqen9zovnQJwSa0BAkAztHUEce1O27aF7ic9JeNLygBcjROR3YRHyqdk1ncS368LpLhayXwNI+pWD5jDihVoe/qnBg7Ldvkdy+DXDRw/AkAFgmM6hx9imYDPJCyG0/r9aXn4prUEFLZvxxbK/rcuYIksuTJG0o4LBUf4rg4kAdQCltZZlwOD9+cD25FdngYBAkEAmHLpYTqgjtITv7VyJ92+4MTE5f+LxpHGHPjgz4XDUHq3XfxXbZijRBFe9cBdbu3nfJxZRpmn6/ofrLb2HdBfMQ==";
//        String content = "UA1TAzo7MDZQlbM2lR7857XAZKlp7X5jYAeyFfG3+qTSuc/w/KZoUQWqku8KhFkB82cle8pyCqPHNPleYJVcy0mEFgf1ztHEHYymRm3QIPMfy8Ie8vZY8FN2rpc5zH6bdrzoSi0VxuGob/BTlXet+/eRHtZJtsWwcRNBKA2LaoU=";
//        String text="abccccc";
//        TestDto testDto = new TestDto().setUserId("1").setAge(20).setUserName("rstyro")
////                .setInfo("info")
//                ;
//
//        String key = AesUtils.generateSecret(256);
//        System.out.println("key="+key);
//        String encodeText = RsaUtils.encodeBase64PublicKey(rsaPublicKey, key);
//        System.out.println("encodeText="+encodeText);
//        System.out.println("decodeTest="+RsaUtils.decodeBase64ByPrivate(rsaPrivateKey, encodeText));
//        String data = JSON.toJSONString(testDto);
//        String aesEncodeData = AesUtils.encodeBase64(data, key);
//        System.out.println("aesEncodeData="+aesEncodeData);

        // 解密
        String rsaPublicKey2 = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCf0B4Al2wIuGK9Bj9Ao23siR2mMfkvdrxEGu2j0tNeA1LSyKOuw7FLmreRMYLCQMI4BTJNYsxUqvdS8IxFpD5hOx9mx6OqY2GQSIZq5a1lt3Rx4SpDiuuVGm7h5uuLN7bvMfaLBW3g4E5DAKapuZ/u5ULO+y2jczVXkaSb1IjNnwIDAQAB";
        String rsaPrivateKey2 = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAJ/QHgCXbAi4Yr0GP0CjbeyJHaYx+S92vEQa7aPS014DUtLIo67DsUuat5ExgsJAwjgFMk1izFSq91LwjEWkPmE7H2bHo6pjYZBIhmrlrWW3dHHhKkOK65UabuHm64s3tu8x9osFbeDgTkMApqm5n+7lQs77LaNzNVeRpJvUiM2fAgMBAAECgYAq4FxcTkPm5wleq4Fm5zIDxxnUUA4J5PJH122wiUy6KWwcL0ZzCf/UR/M+Gil50oQJIaPITVyCzsfCUdVgjdtKL7x8e1dQwlI3/DLEat02Njj4fl6KsMq9EqLyleq0UdgYtevZOOoi+ZKXlqZjkM3yOsbwyu9u0D+s77KfHihwuQJBAODhWKTLywJwSXPC6CvlSoyCjscWgUadk8IN+ELyLq591DYFCQllYQPyMj8Cy0dY5OC9GvwRLZurs9LGi6C9d0UCQQC17a76RNHqmmGKsEEGIx3XIzvDrjSRmE3v+NLMcf+JUaUJiKmedDZeWnJuxIXVmFbHi2bzCb2NXUYqhuXsuJ2TAkBHxSO5VKEx4gxPOcFHYSJtva07tN8FXn0tza+SDiD/54C2zNyZdxWDYOTQX1/pIWHKqA/YqtLXf/EgL+WYI1/RAkAk+RwRgsECo8NlEzLz01kyKtfvicznNgPI3FHC+PwM5UncKSkHqeiOvmT5O/lTEnW4cg1HIVijjSxAYlACDvb/AkBmwlv6+gLoKpCU6h7+J6OxB9GKM2Hjs3Mh5tgXgveCwMg2Knz+RPIj92jq7CLm20xs2654yYnyHc4V+kzr3Zu1";

        String data="LE/E3sspFS1NZFInMy7BBYc1o+/HrglWWLTE/V09cE+osHRa16QS6oAvTPpXHMUtyefTs7POB2OiIS0IVoycTj6yvyvO0IqERfrib1si3ywwLX6Aix7Fl0a7VAYlOYJ3N5/VoTrB/D6L2qozBaG0nMdAtN6o0fa8nVdPOKcIm91AxbaAEhCWWquKq9GMANRZEnD0+HuwWMxongKLOpQblSsrdAILTFs2aNvAt3chlhDiXxomqTlS/LnPUOmHs0y3gFdy9180zflDUnz9biMNGCebPJLGoK5v9bLJYqHH4482VMMkNkp3Fj6jCj6R4JZSOhgWoANS9JccZiN37pnvaS8zSWZcz7lZwPDRno2ke08sYNq6MP3iLsQ/EGLmhwfPWHP2aZvB5uLwX3+N4kxWwhIZT1Mi0jZ0gQLFGAk63OistCpIghqfYePJspODsPHhHQA42ei7L0BqssQ4FnCnVrVsYQOHxgwaHVqxHPCz2mE73BBRq9zg2NXi/k/vOauh";
        String decode = RsaUtils.decodeBase64ByPrivate(rsaPrivateKey2, data);
        System.out.println("decode="+decode);
    }
}

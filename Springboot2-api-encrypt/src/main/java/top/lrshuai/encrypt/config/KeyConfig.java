package top.lrshuai.encrypt.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 密钥配置
 * @author rstyro
 */
@Configuration
public class KeyConfig {

    @Value("${api.encrypt.rsa.publicKey}")
    private String rsaPublicKey;

    @Value("${api.encrypt.rsa.privateKey}")
    private String rsaPrivateKey;

    public String getRsaPrivateKey() {
        return rsaPrivateKey;
    }

    public String getRsaPublicKey() {
        return rsaPublicKey;
    }

}

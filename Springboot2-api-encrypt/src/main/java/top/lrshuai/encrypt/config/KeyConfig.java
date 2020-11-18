package top.lrshuai.encrypt.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 密钥配置
 * @author rstyro
 */
@Configuration
public class KeyConfig {

    /**
     * rsa 公钥
     */
    @Value("${api.encrypt.rsa.publicKey}")
    private String rsaPublicKey;

    /**
     * rsa 私钥
     */
    @Value("${api.encrypt.rsa.privateKey}")
    private String rsaPrivateKey;

    /**
     * 前端rsa 公钥
     */
    @Value("${api.encrypt.rsa.frontPublicKey}")
    private String frontRsaPublicKey;

    /**
     * aes向量 16位
     */
    @Value("${api.encrypt.aes.iv}")
    private String aesIv;

    public String getRsaPrivateKey() {
        return rsaPrivateKey;
    }

    public String getRsaPublicKey() {
        return rsaPublicKey;
    }

    public String getFrontRsaPublicKey() {
        return frontRsaPublicKey;
    }

    public String getAesIv() {
        return aesIv;
    }
}

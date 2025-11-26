package top.lrshuai.jasypt;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.jasypt.util.text.BasicTextEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringbootJasyptApplicationTests {

    private static final String SECRET_KEY = "rstyro_#$%66dashun";


    /**
     * 简单加密
     */
    @Test
    void BasicTextEncryptorTest() {
        // 密钥-替换为你的密钥
        String secretKey = SECRET_KEY;
        // 1. 初始化加密器并设置密钥（需妥善保管）
        BasicTextEncryptor encryptor = new BasicTextEncryptor();
        encryptor.setPassword(secretKey);
        // 2. 加密数据
        String rawData = "rstyro";
        String encryptedData = encryptor.encrypt(rawData);
        // 输出类似 "b+lGxED5CX0CrzhqwdDkRCOyvjUQFAFX"
        System.out.println("加密结果: " + encryptedData);
        // 3. 解密验证（可选）
        String decryptedData = encryptor.decrypt(encryptedData);
        // 应与 rawData 一致
        System.out.println("解密验证: " + decryptedData);

        System.out.println("============");
        System.out.println("============");
        System.out.println("============");
    }

    /**
     * 自定义加密算法
     */
    @Test
    void StandardPBEStringEncryptorTest() {
        // 密钥-替换为你的密钥
        String secretKey = SECRET_KEY;
        System.out.println("secretKey: " + SECRET_KEY);

        // 1. 配置加密器
        StandardPBEStringEncryptor stringEncryptor = new StandardPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        // 加密密钥
        config.setPassword(secretKey);
        // 推荐安全算法
        config.setAlgorithm("PBEWithHMACSHA512AndAES_256");
        // 使用随机IV提升安全性
        config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");
        stringEncryptor.setConfig(config);

        // 2. 加密数据
        String data = "rstyro";
        String encryptedData2 = stringEncryptor.encrypt(data);
        System.out.println("加密结果: " + encryptedData2);

        // 3. 解密
        System.out.println("解密验证: " + stringEncryptor.decrypt(encryptedData2));
    }

}

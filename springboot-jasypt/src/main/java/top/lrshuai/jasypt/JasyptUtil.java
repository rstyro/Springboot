package top.lrshuai.jasypt;

import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Jasypt加密解密工具类
 *
 * @author rstyro
 * @date 2025/11/25
 */
@Slf4j
public class JasyptUtil {

    /**
     * 默认加密秘钥（仅用于开发和测试环境）
     * 生产环境务必通过启动参数或环境变量传入！
     */
    private static final String DEFAULT_SECRET_KEY = "rstyro_#$%66dashun";

    /**
     * 支持的加密算法（按安全性从高到低排序）
     */
    private static final List<String> SUPPORTED_ALGORITHMS = Arrays.asList(
            "PBEWithHMACSHA512AndAES_256",  // 最安全，推荐
            "PBEWithHMACSHA256AndAES_128",  // 较安全
            "PBEWithMD5AndDES"              // 兼容性好，安全性较低
    );

    /**
     * 默认算法
     */
    private static final String DEFAULT_ALGORITHM = SUPPORTED_ALGORITHMS.get(0);

    /**
     * 加密器实例（线程安全，可复用）
     */
    private static volatile StandardPBEStringEncryptor encryptor;

    /**
     * 获取加密器配置
     */
    private static SimpleStringPBEConfig getPBEConfig(String secretKey, String algorithm) {
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(StringUtils.hasText(secretKey) ? secretKey : DEFAULT_SECRET_KEY);
        config.setAlgorithm(validateAlgorithm(algorithm));
        config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator"); // 使用随机IV提升安全性
        config.setKeyObtentionIterations(1000); // 哈希迭代次数
        config.setPoolSize(4); // 加密池大小，提升性能
        return config;
    }

    /**
     * 获取或创建加密器实例（单例模式，线程安全）
     */
    private static StandardPBEStringEncryptor getEncryptor(String secretKey, String algorithm) {
        if (encryptor == null) {
            synchronized (JasyptUtil.class) {
                if (encryptor == null) {
                    encryptor = new StandardPBEStringEncryptor();
                    encryptor.setConfig(getPBEConfig(secretKey, algorithm));
                    log.info("Jasypt加密器初始化完成，使用算法: {}", algorithm);
                }
            }
        }
        return encryptor;
    }

    /**
     * 加密文本（使用默认秘钥和算法）
     */
    public static String encrypt(String plainText) {
        return encrypt(plainText, null, null);
    }

    /**
     * 加密文本（使用指定秘钥）
     */
    public static String encrypt(String plainText, String secretKey) {
        return encrypt(plainText, secretKey, null);
    }

    /**
     * 加密文本（使用指定秘钥和算法）
     */
    public static String encrypt(String plainText, String secretKey, String algorithm) {
        validateInput(plainText, "待加密文本不能为空");
        try {
            return getEncryptor(secretKey, algorithm).encrypt(plainText);
        } catch (Exception e) {
            log.error("加密文本时发生异常，原文: {}", plainText, e);
            throw new RuntimeException("加密失败: " + e.getMessage(), e);
        }
    }

    /**
     * 解密文本（使用默认秘钥和算法）
     */
    public static String decrypt(String encryptedText) {
        return decrypt(encryptedText, null, null);
    }

    /**
     * 解密文本（使用指定秘钥）
     */
    public static String decrypt(String encryptedText, String secretKey) {
        return decrypt(encryptedText, secretKey, null);
    }

    /**
     * 解密文本（使用指定秘钥和算法）
     */
    public static String decrypt(String encryptedText, String secretKey, String algorithm) {
        validateInput(encryptedText, "待解密文本不能为空");
        try {
            return getEncryptor(secretKey, algorithm).decrypt(encryptedText);
        } catch (Exception e) {
            log.error("解密文本时发生异常，密文: {}", encryptedText, e);
            throw new RuntimeException("解密失败: " + e.getMessage(), e);
        }
    }


    /**
     * 验证算法是否支持
     */
    private static String validateAlgorithm(String algorithm) {
        String actualAlgorithm = StringUtils.hasText(algorithm) ? algorithm : DEFAULT_ALGORITHM;
        if (!SUPPORTED_ALGORITHMS.contains(actualAlgorithm)) {
            log.warn("不支持的加密算法: {}，将使用默认算法: {}", actualAlgorithm, DEFAULT_ALGORITHM);
            throw new RuntimeException("不支持的加密算法:"+actualAlgorithm);
        }
        return actualAlgorithm;
    }

    /**
     * 输入参数验证
     */
    private static void validateInput(String input, String errorMessage) {
        if (!StringUtils.hasText(input)) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    /**
     * 重新初始化加密器（用于动态更换秘钥等场景）
     */
    public static synchronized void reinitialize(String newSecretKey, String newAlgorithm) {
        log.info("重新初始化Jasypt加密器...");
        encryptor = null;
        getEncryptor(newSecretKey, newAlgorithm);
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例
        List<String> testCases = Arrays.asList("root", "MySecretPassword123!", "测试中文");

        log.info("开始Jasypt工具类测试...");

        for (String testCase : testCases) {
            try {
                String encrypted = encrypt(testCase);
                String decrypted = decrypt(encrypted);

                System.out.println("原文: " + testCase);
                System.out.println("加密: " + encrypted);
                System.out.println("解密: " + decrypted);
                System.out.println("结果: " + (testCase.equals(decrypted) ? "✓ 成功" : "✗ 失败"));
                System.out.println("---");
            } catch (Exception e) {
                System.err.println("测试用例失败: " + testCase + ", 错误: " + e.getMessage());
            }
        }

        // 测试自定义秘钥
        String customKey = "MyCustomSecretKey@2024";
        String data = "SensitiveData";
        String encryptedWithCustomKey = encrypt(data, customKey);
        String decryptedWithCustomKey = decrypt(encryptedWithCustomKey, customKey);

        System.out.println("自定义秘钥测试:");
        System.out.println("原文: " + data);
        System.out.println("加密: " + encryptedWithCustomKey);
        System.out.println("解密: " + decryptedWithCustomKey);
        System.out.println("结果: " + (data.equals(decryptedWithCustomKey) ? "✓ 成功" : "✗ 失败"));
    }
}
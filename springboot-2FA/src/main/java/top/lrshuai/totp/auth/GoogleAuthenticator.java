package top.lrshuai.totp.auth;


import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

/**
 * Google Authenticator 工具类
 * 基于 TOTP (Time-based One-Time Password) 算法实现双因素认证
 * 支持 HMAC-SHA1、HMAC-SHA256、HMAC-SHA512 算法
 * 参考 RFC 6238 标准，兼容 Google Authenticator 移动应用
 *
 * 主要功能：
 * - 生成随机密钥（支持不同算法推荐长度）
 * - 生成TOTP动态验证码
 * - 生成Google Authenticator可识别的二维码数据
 * - 验证用户输入的验证码
 * - 支持多种HMAC算法
 *
 * @author rstyro
 */
public final class GoogleAuthenticator {

    /** 默认密钥长度（字节）- SHA1 */
    public static final int DEFAULT_SECRET_KEY_LENGTH_SHA1 = 20;
    /** SHA256算法推荐密钥长度（字节） */
    public static final int DEFAULT_SECRET_KEY_LENGTH_SHA256 = 32;
    /** SHA512算法推荐密钥长度（字节） */
    public static final int DEFAULT_SECRET_KEY_LENGTH_SHA512 = 64;

    /** 默认密钥长度 */
    private static final int DEFAULT_SECRET_KEY_LENGTH = DEFAULT_SECRET_KEY_LENGTH_SHA1;

    /** 默认时间窗口大小（30秒单位） */
    private static final int DEFAULT_WINDOW_SIZE = 2;
    /** 最大允许的时间窗口大小 */
    private static final int MAX_WINDOW_SIZE = 17;
    /** 时间步长（秒） */
    private static final long TIME_STEP = 30L;
    /** 验证码位数 */
    private static final int CODE_DIGITS = 6;

    /** 算法名称常量 */
    public static final String HMAC_SHA1 = "HmacSHA1";
    public static final String HMAC_SHA256 = "HmacSHA256";
    public static final String HMAC_SHA512 = "HmacSHA512";

    /** 默认算法 */
    private static final String DEFAULT_ALGORITHM = HMAC_SHA1;

    /** 算法对应的推荐密钥长度映射 */
    private static final Map<String, Integer> ALGORITHM_KEY_LENGTH_MAP = new HashMap<>();

    static {
        ALGORITHM_KEY_LENGTH_MAP.put(HMAC_SHA1, DEFAULT_SECRET_KEY_LENGTH_SHA1);
        ALGORITHM_KEY_LENGTH_MAP.put(HMAC_SHA256, DEFAULT_SECRET_KEY_LENGTH_SHA256);
        ALGORITHM_KEY_LENGTH_MAP.put(HMAC_SHA512, DEFAULT_SECRET_KEY_LENGTH_SHA512);
    }

    /** 当前时间窗口大小 */
    private static int windowSize = DEFAULT_WINDOW_SIZE;
    /** 当前使用的算法 */
    private static String currentAlgorithm = DEFAULT_ALGORITHM;

    /**
     * 私有构造方法，防止实例化
     */
    private GoogleAuthenticator() {
        throw new AssertionError("GoogleAuthenticator是工具类，不能实例化");
    }

    // ==================== 密钥生成相关方法 ====================

    /**
     * 生成随机的Base32编码密钥（使用默认算法和长度）
     * 密钥用于在客户端和服务器端之间共享，用于生成验证码
     *
     * @return Base32编码的随机密钥（大写，无分隔符）
     * @throws SecurityException 如果随机数生成失败
     */
    public static String generateRandomSecretKey() {
        return generateRandomSecretKey(DEFAULT_SECRET_KEY_LENGTH);
    }

    /**
     * 生成指定长度的随机Base32编码密钥
     *
     * @param length 密钥长度（字节）
     * @return Base32编码的随机密钥
     */
    public static String generateRandomSecretKey(int length) {
        try {
            SecureRandom random = SecureRandom.getInstanceStrong();
            byte[] bytes = new byte[length];
            random.nextBytes(bytes);

            Base32 base32 = new Base32();
            return base32.encodeToString(bytes).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            throw new SecurityException("安全随机数生成器不可用", e);
        }
    }

    /**
     * 为指定算法生成推荐长度的随机密钥
     *
     * @param algorithm 算法（HMAC_SHA1, HMAC_SHA256, HMAC_SHA512）
     * @return Base32编码的随机密钥
     */
    public static String generateRandomSecretKey(String algorithm) {
        Integer length = ALGORITHM_KEY_LENGTH_MAP.get(algorithm);
        if (length == null) {
            throw new IllegalArgumentException("不支持的算法: " + algorithm +
                    "，支持的算法: " + ALGORITHM_KEY_LENGTH_MAP.keySet());
        }
        return generateRandomSecretKey(length);
    }

    /**
     * 生成指定算法和长度的随机密钥
     *
     * @param algorithm 算法
     * @param length 密钥长度
     * @return Base32编码的随机密钥
     */
    public static String generateRandomSecretKey(String algorithm, int length) {
        Integer recommendedLength = ALGORITHM_KEY_LENGTH_MAP.get(algorithm);
        if (recommendedLength != null && length < recommendedLength) {
            System.err.println("警告: 密钥长度" + length + "字节小于" + algorithm +
                    "推荐长度" + recommendedLength + "字节，可能存在安全风险");
        }
        return generateRandomSecretKey(length);
    }

    // ==================== TOTP生成方法 ====================

    /**
     * 生成当前时间的TOTP验证码（默认SHA1算法）
     *
     * @param secretKey Base32编码的共享密钥
     * @return 6位数字的TOTP验证码
     * @throws IllegalArgumentException 如果密钥为空或格式错误
     * @throws SecurityException 如果加密操作失败
     */
    public static String generateTOTPCode(String secretKey) {
        return generateTOTPCode(secretKey, DEFAULT_ALGORITHM);
    }

    /**
     * 生成当前时间的TOTP验证码（指定算法）
     *
     * @param secretKey Base32编码的共享密钥
     * @param algorithm 算法（HMAC_SHA1, HMAC_SHA256, HMAC_SHA512）
     * @return 6位数字的TOTP验证码
     */
    public static String generateTOTPCode(String secretKey, String algorithm) {
        validateSecretKey(secretKey);
        validateAlgorithm(algorithm);

        try {
            // 标准化密钥：移除空格并转为大写
            String normalizedKey = secretKey.replace(" ", "").toUpperCase();
            Base32 base32 = new Base32();
            byte[] decodedBytes = base32.decode(normalizedKey);
            String hexKey = Hex.encodeHexString(decodedBytes);

            // 计算当前时间窗口
            long timeWindow = (System.currentTimeMillis() / 1000L) / TIME_STEP;
            String hexTime = Long.toHexString(timeWindow);

            // 调用TOTP类的对应方法
            switch (algorithm) {
                case HMAC_SHA256:
                    return TOTP.generateTOTP256(hexKey, hexTime, CODE_DIGITS);
                case HMAC_SHA512:
                    return TOTP.generateTOTP512(hexKey, hexTime, CODE_DIGITS);
                case HMAC_SHA1:
                default:
                    return TOTP.generateTOTP(hexKey, hexTime, CODE_DIGITS, algorithm);
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("无效的密钥格式: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new SecurityException("生成TOTP验证码失败: " + e.getMessage(), e);
        }
    }

    /**
     * 生成当前时间的TOTP验证码（SHA256算法）
     *
     * @param secretKey Base32编码的共享密钥
     * @return 6位数字的TOTP验证码
     */
    public static String generateTOTPCode256(String secretKey) {
        return generateTOTPCode(secretKey, HMAC_SHA256);
    }

    /**
     * 生成当前时间的TOTP验证码（SHA512算法）
     *
     * @param secretKey Base32编码的共享密钥
     * @return 6位数字的TOTP验证码
     */
    public static String generateTOTPCode512(String secretKey) {
        return generateTOTPCode(secretKey, HMAC_SHA512);
    }

    /**
     * 生成指定时间戳的TOTP验证码
     *
     * @param secretKey Base32编码的共享密钥
     * @param timestamp 时间戳（毫秒）
     * @param algorithm 算法
     * @return 6位数字的TOTP验证码
     */
    public static String generateTOTPCode(String secretKey, long timestamp, String algorithm) {
        validateSecretKey(secretKey);
        validateAlgorithm(algorithm);

        try {
            String normalizedKey = secretKey.replace(" ", "").toUpperCase();
            Base32 base32 = new Base32();
            byte[] decodedBytes = base32.decode(normalizedKey);
            String hexKey = Hex.encodeHexString(decodedBytes);

            long timeWindow = (timestamp / 1000L) / TIME_STEP;
            String hexTime = Long.toHexString(timeWindow);

            switch (algorithm) {
                case HMAC_SHA256:
                    return TOTP.generateTOTP256(hexKey, hexTime, CODE_DIGITS);
                case HMAC_SHA512:
                    return TOTP.generateTOTP512(hexKey, hexTime, CODE_DIGITS);
                case HMAC_SHA1:
                default:
                    return TOTP.generateTOTP(hexKey, hexTime, CODE_DIGITS, algorithm);
            }
        } catch (Exception e) {
            throw new SecurityException("生成TOTP验证码失败: " + e.getMessage(), e);
        }
    }

    // ==================== 二维码生成方法 ====================

    /**
     * 生成Google Authenticator二维码内容URL（默认SHA1算法）
     *
     * @param secretKey 共享密钥
     * @param account 用户账号（如邮箱或用户名）
     * @param issuer 发行者名称（应用或网站名称）
     * @return 二维码内容URL
     * @throws IllegalArgumentException 如果参数为空或格式错误
     */
    public static String generateQRCodeUrl(String secretKey, String account, String issuer) {
        return generateQRCodeUrl(secretKey, account, issuer, DEFAULT_ALGORITHM);
    }

    /**
     * 生成Google Authenticator二维码内容URL（指定算法）
     * 注意：Google Authenticator应用可能不支持SHA256/SHA512
     *
     * @param secretKey 共享密钥
     * @param account 用户账号
     * @param issuer 发行者名称
     * @param algorithm 算法
     * @return 二维码内容URL
     */
    public static String generateQRCodeUrl(String secretKey, String account, String issuer, String algorithm) {
        validateParameters(secretKey, account, issuer);
        validateAlgorithm(algorithm);

        String normalizedKey = secretKey.replace(" ", "").toUpperCase();

        // 构建OTP Auth URL，符合Google Authenticator标准格式
        StringBuilder url = new StringBuilder("otpauth://totp/")
                .append(URLEncoder.encode(issuer + ":" + account, StandardCharsets.UTF_8).replace("+", "%20"))
                .append("?secret=").append(URLEncoder.encode(normalizedKey, StandardCharsets.UTF_8).replace("+", "%20"))
                .append("&issuer=").append(URLEncoder.encode(issuer, StandardCharsets.UTF_8).replace("+", "%20"));

        // 添加算法参数（SHA1是默认值，可以省略）
        if (!HMAC_SHA1.equals(algorithm)) {
            url.append("&algorithm=").append(algorithm.toUpperCase());
        }

        // 添加位数参数
        url.append("&digits=").append(CODE_DIGITS);

        // 添加时间步长参数
        url.append("&period=").append(TIME_STEP);

        return url.toString();
    }

    // ==================== 验证方法 ====================

    /**
     * 验证TOTP验证码（默认SHA1算法）
     * 考虑时间窗口偏移，以处理客户端和服务端之间的时间差异
     *
     * @param secretKey 共享密钥
     * @param code 待验证的验证码
     * @param timestamp 时间戳（毫秒）
     * @return 验证是否成功
     * @throws IllegalArgumentException 如果参数无效
     */
    public static boolean verifyCode(String secretKey, long code, long timestamp) {
        return verifyCode(secretKey, code, timestamp, DEFAULT_ALGORITHM);
    }

    /**
     * 验证TOTP验证码（指定算法）
     *
     * @param secretKey 共享密钥
     * @param code 待验证的验证码
     * @param timestamp 时间戳（毫秒）
     * @param algorithm 算法
     * @return 验证是否成功
     */
    public static boolean verifyCode(String secretKey, long code, long timestamp, String algorithm) {
        validateSecretKey(secretKey);
        validateAlgorithm(algorithm);

        if (code < 0 || code > 999999) {
            throw new IllegalArgumentException("验证码必须是6位数字");
        }

        // 计算基准时间窗口
        long timeWindow = (timestamp / 1000L) / TIME_STEP;

        // 检查当前及前后时间窗口内的验证码
        for (int i = -windowSize; i <= windowSize; i++) {
            try {
                String generatedCode = generateTOTPCode(secretKey, timestamp + (i * TIME_STEP * 1000L), algorithm);
                if (Long.parseLong(generatedCode) == code) {
                    return true;
                }
            } catch (Exception e) {
                // 记录日志但继续检查其他时间窗口
                System.err.println("验证码验证过程中出现异常: " + e.getMessage());
            }
        }

        return false;
    }

    /**
     * 验证当前时间的TOTP验证码（默认SHA1算法）
     *
     * @param secretKey 共享密钥
     * @param code 待验证的验证码
     * @return 验证是否成功
     */
    public static boolean verifyCurrentCode(String secretKey, long code) {
        return verifyCode(secretKey, code, System.currentTimeMillis(), DEFAULT_ALGORITHM);
    }

    /**
     * 验证当前时间的TOTP验证码（指定算法）
     *
     * @param secretKey 共享密钥
     * @param code 待验证的验证码
     * @param algorithm 算法
     * @return 验证是否成功
     */
    public static boolean verifyCurrentCode(String secretKey, long code, String algorithm) {
        return verifyCode(secretKey, code, System.currentTimeMillis(), algorithm);
    }

    /**
     * 验证当前时间的TOTP验证码字符串（更易用的方法）
     *
     * @param secretKey 共享密钥
     * @param code 待验证的验证码字符串
     * @return 验证是否成功
     */
    public static boolean verifyCurrentCode(String secretKey, String code) {
        return verifyCurrentCode(secretKey, code, DEFAULT_ALGORITHM);
    }

    /**
     * 验证当前时间的TOTP验证码字符串（指定算法）
     *
     * @param secretKey 共享密钥
     * @param code 待验证的验证码字符串
     * @param algorithm 算法
     * @return 验证是否成功
     */
    public static boolean verifyCurrentCode(String secretKey, String code, String algorithm) {
        try {
            long codeValue = Long.parseLong(code);
            return verifyCurrentCode(secretKey, codeValue, algorithm);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 验证当前时间的TOTP验证码字符串（SHA256算法）
     *
     * @param secretKey 共享密钥
     * @param code 待验证的验证码字符串
     * @return 验证是否成功
     */
    public static boolean verifyCurrentCode256(String secretKey, String code) {
        return verifyCurrentCode(secretKey, code, HMAC_SHA256);
    }

    /**
     * 验证当前时间的TOTP验证码字符串（SHA512算法）
     *
     * @param secretKey 共享密钥
     * @param code 待验证的验证码字符串
     * @return 验证是否成功
     */
    public static boolean verifyCurrentCode512(String secretKey, String code) {
        return verifyCurrentCode(secretKey, code, HMAC_SHA512);
    }

    // ==================== 配置方法 ====================

    /**
     * 设置验证时间窗口大小
     * 时间窗口大小决定了允许的时间偏移范围（每个窗口30秒）
     * @param size 窗口大小（1-17）
     * @throws IllegalArgumentException 如果窗口大小超出范围
     */
    public static void setWindowSize(int size) {
        if (size < 1 || size > MAX_WINDOW_SIZE) {
            throw new IllegalArgumentException("窗口大小必须在1到" + MAX_WINDOW_SIZE + "之间");
        }
        windowSize = size;
    }

    /**
     * 设置默认算法
     *
     * @param algorithm 算法（HMAC_SHA1, HMAC_SHA256, HMAC_SHA512）
     */
    public static void setDefaultAlgorithm(String algorithm) {
        validateAlgorithm(algorithm);
        currentAlgorithm = algorithm;
    }

    /**
     * 获取当前时间窗口大小
     *
     * @return 当前时间窗口大小
     */
    public static int getWindowSize() {
        return windowSize;
    }

    /**
     * 获取当前默认算法
     *
     * @return 当前算法
     */
    public static String getDefaultAlgorithm() {
        return currentAlgorithm;
    }

    /**
     * 获取算法对应的推荐密钥长度
     *
     * @param algorithm 算法
     * @return 推荐密钥长度（字节）
     */
    public static int getRecommendedKeyLength(String algorithm) {
        Integer length = ALGORITHM_KEY_LENGTH_MAP.get(algorithm);
        if (length == null) {
            throw new IllegalArgumentException("不支持的算法: " + algorithm);
        }
        return length;
    }

    /**
     * 获取支持的算法列表
     *
     * @return 支持的算法名称数组
     */
    public static String[] getSupportedAlgorithms() {
        return new String[]{HMAC_SHA1, HMAC_SHA256, HMAC_SHA512};
    }

    // ==================== 辅助方法 ====================

    /**
     * 验证密钥格式
     */
    private static void validateSecretKey(String secretKey) {
        if (secretKey == null || secretKey.trim().isEmpty()) {
            throw new IllegalArgumentException("密钥不能为空");
        }
        if (!secretKey.matches("^[A-Z2-7=\\s]+$")) {
            throw new IllegalArgumentException("密钥必须包含有效的Base32字符（A-Z, 2-7）");
        }
    }

    /**
     * 验证算法
     */
    private static void validateAlgorithm(String algorithm) {
        if (!ALGORITHM_KEY_LENGTH_MAP.containsKey(algorithm)) {
            throw new IllegalArgumentException("不支持的算法: " + algorithm +
                    "，支持的算法: " + String.join(", ", ALGORITHM_KEY_LENGTH_MAP.keySet()));
        }
    }

    /**
     * 验证二维码生成参数
     */
    private static void validateParameters(String secretKey, String account, String issuer) {
        validateSecretKey(secretKey);

        if (account == null || account.trim().isEmpty()) {
            throw new IllegalArgumentException("账号不能为空");
        }
        if (issuer == null || issuer.trim().isEmpty()) {
            throw new IllegalArgumentException("发行者名称不能为空");
        }
    }

    /**
     * 获取算法的显示名称
     *
     * @param algorithm 算法标识
     * @return 显示名称
     */
    public static String getAlgorithmDisplayName(String algorithm) {
        switch (algorithm) {
            case HMAC_SHA1: return "HMAC-SHA1";
            case HMAC_SHA256: return "HMAC-SHA256";
            case HMAC_SHA512: return "HMAC-SHA512";
            default: return algorithm;
        }
    }

    // ==================== 测试方法 ====================

    /**
     * 完整测试示例
     */
    public static void testAllAlgorithms() {
        System.out.println("=== Google Authenticator 多算法测试 ===\n");

        String[] algorithms = {HMAC_SHA1, HMAC_SHA256, HMAC_SHA512};

        for (String algorithm : algorithms) {
            System.out.println("\n--- 测试 " + getAlgorithmDisplayName(algorithm) + " 算法 ---");

            // 生成密钥
            String secretKey = generateRandomSecretKey(algorithm);
            int recommendedLength = getRecommendedKeyLength(algorithm);
            System.out.println("1. 生成密钥 (" + recommendedLength + "字节): " + secretKey);

            // 生成当前验证码
            String totpCode = generateTOTPCode(secretKey, algorithm);
            System.out.println("2. 当前TOTP验证码: " + totpCode);

            // 生成二维码URL
            String qrCodeUrl = generateQRCodeUrl(secretKey, "test@example.com", "TOTP-Test", algorithm);
            System.out.println("3. 二维码URL: " + (qrCodeUrl.length() > 100 ? qrCodeUrl.substring(0, 100) + "..." : qrCodeUrl));

            // 验证验证码
            boolean isValid = verifyCurrentCode(secretKey, totpCode, algorithm);
            System.out.println("4. 验证码验证结果: " + (isValid ? "✓ 通过" : "✗ 失败"));

            // 错误验证码测试
            boolean isInvalid = verifyCurrentCode(secretKey, "123456", algorithm);
            System.out.println("5. 错误验证码测试: " + (!isInvalid ? "✓ 测试通过" : "✗ 测试不通过-错误验证码也通过"));
        }

        System.out.println("\n=== 测试完成 ===");
    }

    /**
     * 主方法：测试多算法支持
     */
    public static void main(String[] args) {
        // 测试所有算法
//        testAllAlgorithms();

        // 或者单独测试特定算法
        String secretKey = "NIHMRAK5ZS73PC3HOAGDTK65QDNCZ6QY";
        String totp1 = generateTOTPCode(secretKey);
        System.out.println("URL: " +generateQRCodeUrl(secretKey, "test-sha1@example.com", "TOTP-Test", HMAC_SHA1));
        System.out.println("当前SHA1验证码: " + totp1);
        System.out.println("当前SHA1验证码: " + verifyCurrentCode(secretKey, "050761"));
        System.out.println();

        String secretKey256 = "VBS6IG6VLRSRVPZUQBFM6G6WE6YGXRF7SCFTUVBJPTWUMPRBAWVQ====";
        String totp256 = generateTOTPCode256(secretKey256);
        System.out.println("URL: " +generateQRCodeUrl(secretKey256, "testsha256@example.com", "TOTP-Test", HMAC_SHA256));
        System.out.println("当前SHA256验证码: " + totp256);
        System.out.println("当前SHA256验证码: " + verifyCurrentCode256(secretKey256, "794120"));
        System.out.println();

        String secretKey512 = "Z535MJVUZWDXKRXHB7LMDS7YMTZOEZE37ZUXAXF6TKMU4MLOZGCHFFPAPY43EMW7MUZJZ7W74T2PFCEUVWRN4Z36XXGPZIX6W7XVIKI=";
        String totp512 = generateTOTPCode256(secretKey512);
        System.out.println("URL: " +generateQRCodeUrl(secretKey512, "testsha512@example.com", "TOTP-Test", HMAC_SHA512));
        System.out.println("当前SHA512验证码: " + totp512);
        System.out.println("当前SHA512验证码: " + verifyCurrentCode512(secretKey512, "149488"));

    }
}
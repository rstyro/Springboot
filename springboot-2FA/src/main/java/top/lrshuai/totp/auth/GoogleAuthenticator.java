package top.lrshuai.totp.auth;


import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Google Authenticator 工具类
 * 基于 TOTP (Time-based One-Time Password) 算法实现双因素认证
 * 参考 RFC 6238 标准，兼容 Google Authenticator 移动应用
 *
 * 主要功能：
 * - 生成随机密钥
 * - 生成TOTP动态验证码
 * - 生成Google Authenticator可识别的二维码数据
 * - 验证用户输入的验证码
 *
 * @author rstyro
 */
public final class GoogleAuthenticator {

    /** 默认密钥长度（字节） */
    private static final int DEFAULT_SECRET_KEY_LENGTH = 20;
    /** 默认时间窗口大小（30秒单位） */
    private static final int DEFAULT_WINDOW_SIZE = 2;
    /** 最大允许的时间窗口大小 */
    private static final int MAX_WINDOW_SIZE = 17;
    /** 时间步长（秒） */
    private static final long TIME_STEP = 30L;
    /** 验证码位数 */
    private static final int CODE_DIGITS = 6;
    /** HMAC算法名称 */
    private static final String HMAC_ALGORITHM = "HmacSHA1";

    /** 当前时间窗口大小 */
    private static int windowSize = DEFAULT_WINDOW_SIZE;

    /**
     * 私有构造方法，防止实例化
     */
    private GoogleAuthenticator() {
        throw new AssertionError("GoogleAuthenticator是工具类，不能实例化");
    }

    /**
     * 生成随机的Base32编码密钥
     * 密钥用于在客户端和服务器端之间共享，用于生成验证码
     *
     * @return Base32编码的随机密钥（大写，无分隔符）
     * @throws SecurityException 如果随机数生成失败
     */
    public static String generateRandomSecretKey() {
        try {
            SecureRandom random = SecureRandom.getInstanceStrong();
            byte[] bytes = new byte[DEFAULT_SECRET_KEY_LENGTH];
            random.nextBytes(bytes);

            Base32 base32 = new Base32();
            return base32.encodeToString(bytes).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            throw new SecurityException("安全随机数生成器不可用", e);
        }
    }

    /**
     * 生成当前时间的TOTP验证码
     *
     * @param secretKey Base32编码的共享密钥
     * @return 6位数字的TOTP验证码
     * @throws IllegalArgumentException 如果密钥为空或格式错误
     * @throws SecurityException 如果加密操作失败
     */
    public static String generateTOTPCode(String secretKey) {
        validateSecretKey(secretKey);

        try {
            // 标准化密钥：移除空格并转为大写
            String normalizedKey = secretKey.replace(" ", "").toUpperCase();
            Base32 base32 = new Base32();
            byte[] decodedBytes = base32.decode(normalizedKey);
            String hexKey = Hex.encodeHexString(decodedBytes);

            // 计算当前时间窗口
            long timeWindow = (System.currentTimeMillis() / 1000L) / TIME_STEP;
            String hexTime = Long.toHexString(timeWindow);

            return TOTP.generateTOTP(hexKey, hexTime, CODE_DIGITS, HMAC_ALGORITHM);

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("无效的密钥格式: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new SecurityException("生成TOTP验证码失败", e);
        }
    }

    /**
     * 生成Google Authenticator二维码内容URL
     * 该URL可用于生成二维码，供Google Authenticator应用扫描
     *
     * @param secretKey 共享密钥
     * @param account 用户账号（如邮箱或用户名）
     * @param issuer 发行者名称（应用或网站名称）
     * @return 二维码内容URL
     * @throws IllegalArgumentException 如果参数为空或格式错误
     */
    public static String generateQRCodeUrl(String secretKey, String account, String issuer) {
        validateParameters(secretKey, account, issuer);

        String normalizedKey = secretKey.replace(" ", "").toUpperCase();

        // 构建OTP Auth URL，符合Google Authenticator标准格式
        String url = "otpauth://totp/"
                + URLEncoder.encode(issuer + ":" + account, StandardCharsets.UTF_8).replace("+", "%20")
                + "?secret=" + URLEncoder.encode(normalizedKey, StandardCharsets.UTF_8).replace("+", "%20")
                + "&issuer=" + URLEncoder.encode(issuer, StandardCharsets.UTF_8).replace("+", "%20");
        return url;

    }

    /**
     * 验证TOTP验证码
     * 考虑时间窗口偏移，以处理客户端和服务端之间的时间差异
     *
     * @param secretKey 共享密钥
     * @param code 待验证的验证码
     * @param timestamp 时间戳（毫秒）
     * @return 验证是否成功
     * @throws IllegalArgumentException 如果参数无效
     */
    public static boolean verifyCode(String secretKey, long code, long timestamp) {
        validateSecretKey(secretKey);

        if (code < 0 || code > 999999) {
            throw new IllegalArgumentException("验证码必须是6位数字");
        }

        Base32 codec = new Base32();
        String normalizedKey = secretKey.replace(" ", "").toUpperCase();
        byte[] decodedKey;

        try {
            decodedKey = codec.decode(normalizedKey);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("无效的Base32密钥格式", e);
        }

        // 计算基准时间窗口
        long timeWindow = (timestamp / 1000L) / TIME_STEP;

        // 检查当前及前后时间窗口内的验证码
        for (int i = -windowSize; i <= windowSize; i++) {
            try {
                long generatedCode = generateVerificationCode(decodedKey, timeWindow + i);
                if (generatedCode == code) {
                    return true;
                }
            } catch (GeneralSecurityException e) {
                // 记录日志但继续检查其他时间窗口
                System.err.println("验证码生成过程中出现安全异常: " + e.getMessage());
            }
        }

        return false;
    }

    /**
     * 验证当前时间的TOTP验证码（便捷方法）
     *
     * @param secretKey 共享密钥
     * @param code 待验证的验证码
     * @return 验证是否成功
     */
    public static boolean verifyCurrentCode(String secretKey, long code) {
        return verifyCode(secretKey, code, System.currentTimeMillis());
    }

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
     * 获取当前时间窗口大小
     *
     * @return 当前时间窗口大小
     */
    public static int getWindowSize() {
        return windowSize;
    }

    /**
     * 生成指定时间窗口的验证码
     *
     * @param key 解码后的密钥字节数组
     * @param timeWindow 时间窗口值
     * @return 验证码数字
     * @throws NoSuchAlgorithmException 如果HMAC-SHA1算法不可用
     * @throws InvalidKeyException 如果密钥无效
     */
    private static long generateVerificationCode(byte[] key, long timeWindow)
            throws NoSuchAlgorithmException, InvalidKeyException {

        // 将时间窗口值转换为8字节数组（大端序）
        byte[] data = new byte[8];
        for (int i = 8; i-- > 0; timeWindow >>>= 8) {
            data[i] = (byte) timeWindow;
        }

        // 计算HMAC-SHA1哈希
        SecretKeySpec signKey = new SecretKeySpec(key, HMAC_ALGORITHM);
        Mac mac = Mac.getInstance(HMAC_ALGORITHM);
        mac.init(signKey);
        byte[] hash = mac.doFinal(data);

        // 动态截取（基于RFC 4226标准）
        int offset = hash[hash.length - 1] & 0x0F;

        // 从偏移位置提取4字节
        long truncatedHash = 0;
        for (int i = 0; i < 4; i++) {
            truncatedHash <<= 8;
            truncatedHash |= (hash[offset + i] & 0xFF);
        }

        // 取模得到6位数
        truncatedHash &= 0x7FFFFFFF;
        truncatedHash %= 1000000;

        return truncatedHash;
    }

    /**
     * 验证密钥格式
     */
    private static void validateSecretKey(String secretKey) {
        if (secretKey == null || secretKey.trim().isEmpty()) {
            throw new IllegalArgumentException("密钥不能为空");
        }
        if (!secretKey.matches("^[A-Z2-7\\s]+$")) {
            throw new IllegalArgumentException("密钥必须包含有效的Base32字符（A-Z, 2-7）");
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
     * 测试示例
     */
    private static void testExample(){
        System.out.println("=== Google Authenticator 测试 ===\n");

        // 生成测试密钥
        String secretKey = generateRandomSecretKey();
        System.out.println("1. 生成的密钥: " + secretKey);

        // 生成当前验证码
        String totpCode = generateTOTPCode(secretKey);
        System.out.println("2. 当前TOTP验证码: " + totpCode);

        // 生成二维码URL
        String qrCodeUrl = generateQRCodeUrl(secretKey, "16888888@qq.com", "2FA-DEMO");
        System.out.println("3. 二维码URL: " + qrCodeUrl);

        // 验证验证码（使用当前生成的验证码进行验证）
        boolean isValid = verifyCurrentCode(secretKey, Long.parseLong(totpCode));
        System.out.println("4. 验证码验证结果: " + (isValid ? "通过" : "失败"));

        // 错误验证码测试
        boolean isInvalid = verifyCurrentCode(secretKey, 123456L);
        System.out.println("5. 错误验证码测试: " + (isInvalid ? "测试不通过-错误验证码也通过" : "测试通过-验证码错误"));

        System.out.println("\n=== 测试完成 ===");
    }

    /**
     * 测试示例
     */
    public static void main(String[] args) {
//        testExample();

        String secretKey = "JDFVW66IN54KRAEQRSS2WQJSC4I54WG3";
        System.out.println("2FA验证结果: "+GoogleAuthenticator.verifyCurrentCode(secretKey, 255415));
        System.out.println("2FA验证结果: "+GoogleAuthenticator.verifyCurrentCode(secretKey, 359926));

    }
}
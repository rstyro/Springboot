package top.lrshuai.totp.auth;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * TOTP (Time-based One-Time Password) 算法实现
 * 基于 RFC 6238 标准，用于生成基于时间的一次性密码。
 * 该类是工具类，所有方法均为静态方法，不可实例化。
 * 功能特点：
 * - 支持 HMAC-SHA1、HMAC-SHA256、HMAC-SHA512 算法
 * - 可自定义密码位数（1-8位）和时间步长
 * - 提供密码验证功能，支持时间偏移容错
 * 使用示例：
 * String key = "3132333435363738393031323334353637383930";
 * String totp = TOTP.generateCurrentTOTP(key);
 * boolean isValid = TOTP.verifyTOTP(key, "123456");
 *
 * @author rstyro
 */
public final class TOTP {

    /**
     * 数字幂数组，用于计算10的n次方，索引对应位数（1-8位）
     * 例如：DIGITS_POWER[6] = 1000000
     */
    private static final int[] DIGITS_POWER = {1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000};

    /** HMAC-SHA1 算法标识 */
    public static final String HMAC_SHA1 = "HmacSHA1";
    /** HMAC-SHA256 算法标识 */
    public static final String HMAC_SHA256 = "HmacSHA256";
    /** HMAC-SHA512 算法标识 */
    public static final String HMAC_SHA512 = "HmacSHA512";

    /** 默认动态密码位数（6位） */
    private static final int DEFAULT_DIGITS = 6;
    /** 默认时间步长（秒） */
    private static final long DEFAULT_TIME_STEP = 30L;
    /** 默认起始时间（Unix纪元） */
    private static final long DEFAULT_START_TIME = 0L;
    /** 默认验证时间窗口大小（允许前后偏移的步数） */
    private static final int DEFAULT_TIME_WINDOW = 1;

    /**
     * 私有构造方法，防止类实例化
     * 工具类应避免实例化，所有方法均为静态方法
     */
    private TOTP() {
        throw new AssertionError("TOTP 是工具类，不能实例化");
    }

    /**
     * 使用HMAC算法计算哈希值
     *
     * @param crypto   加密算法 (HmacSHA1, HmacSHA256, HmacSHA512)
     * @param keyBytes 密钥字节数组
     * @param text     要认证的消息文本
     * @return HMAC哈希值
     * @throws GeneralSecurityException 安全算法异常
     */
    private static byte[] hmacSha(String crypto, byte[] keyBytes, byte[] text)
            throws GeneralSecurityException {
        Mac hmac = Mac.getInstance(crypto);
        SecretKeySpec macKey = new SecretKeySpec(keyBytes, "RAW");
        hmac.init(macKey);
        return hmac.doFinal(text);
    }

    /**
     * 将十六进制字符串转换为字节数组
     *
     * @param hex 十六进制字符串
     * @return 字节数组
     * @throws IllegalArgumentException 当十六进制字符串格式错误时
     */
    private static byte[] hexStr2Bytes(String hex) {
        // 使用BigInteger处理十六进制字符串，确保正确转换
        byte[] bArray = new BigInteger("10" + hex, 16).toByteArray();
        byte[] ret = new byte[bArray.length - 1];
        System.arraycopy(bArray, 1, ret, 0, ret.length);
        return ret;
    }

    /**
     * 生成TOTP值
     *
     * @param key          共享密钥，十六进制编码字符串
     * @param time         时间计数器值，十六进制编码字符串
     * @param returnDigits  返回的TOTP位数，必须在1到8之间
     * @param crypto       加密算法，如 "HmacSHA1"
     * @return TOTP数值字符串，指定位数
     * @throws IllegalArgumentException 如果位数无效或参数错误
     * @throws RuntimeException 如果安全算法出错
     */
    public static String generateTOTP(String key, String time, int returnDigits, String crypto) {
        // 参数校验
        if (returnDigits < 1 || returnDigits > 8) {
            throw new IllegalArgumentException("TOTP位数必须在1到8之间");
        }
        if (key == null || key.isEmpty() || time == null || time.isEmpty()) {
            throw new IllegalArgumentException("密钥和时间参数不能为空");
        }

        // 时间字符串填充至16字符（64位十六进制表示）
        String paddedTime = time;
        while (paddedTime.length() < 16) {
            paddedTime = "0" + paddedTime;
        }

        try {
            byte[] msg = hexStr2Bytes(paddedTime);
            byte[] k = hexStr2Bytes(key);
            byte[] hash = hmacSha(crypto, k, msg);

            // 动态截取：取最后一字节的低4位作为偏移量
            int offset = hash[hash.length - 1] & 0x0f;

            // 从偏移位置取4字节，按大端序组合为整数
            int binary = ((hash[offset] & 0x7f) << 24)
                    | ((hash[offset + 1] & 0xff) << 16)
                    | ((hash[offset + 2] & 0xff) << 8)
                    | (hash[offset + 3] & 0xff);

            // 取模得到指定位数的TOTP值
            int otp = binary % DIGITS_POWER[returnDigits];

            // 格式化为指定位数字符串，不足位补零
            return String.format("%0" + returnDigits + "d", otp);

        } catch (GeneralSecurityException e) {
            throw new RuntimeException("TOTP生成安全错误: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("TOTP生成失败: " + e.getMessage(), e);
        }
    }


    /**
     * 生成TOTP（默认6位数，HMAC-SHA1算法）
     */
    public static String generateTOTP(String key, String time) {
        return generateTOTP(key, time, DEFAULT_DIGITS, HMAC_SHA1);
    }

    /**
     * 生成TOTP（指定位数，HMAC-SHA1算法）
     */
    public static String generateTOTP(String key, String time, int returnDigits) {
        return generateTOTP(key, time, returnDigits, HMAC_SHA1);
    }

    /**
     * 生成TOTP（指定位数，HMAC-SHA256算法）
     */
    public static String generateTOTP256(String key, String time, int returnDigits) {
        return generateTOTP(key, time, returnDigits, HMAC_SHA256);
    }

    /**
     * 生成TOTP（指定位数，HMAC-SHA512算法）
     */
    public static String generateTOTP512(String key, String time, int returnDigits) {
        return generateTOTP(key, time, returnDigits, HMAC_SHA512);
    }

    /**
     * 基于当前时间生成TOTP
     * @param key 共享密钥（十六进制字符串）
     * @return TOTP值（6位数）
     */
    public static String generateCurrentTOTP(String key) {
        long currentTime = System.currentTimeMillis() / 1000;
        long timeStep = (currentTime - DEFAULT_START_TIME) / DEFAULT_TIME_STEP;
        return generateTOTP(key, Long.toHexString(timeStep).toUpperCase());
    }

    /**
     * 验证TOTP代码，考虑时间偏移容错
     *
     * @param key  共享密钥
     * @param code 要验证的代码
     * @param timeWindow 时间窗口大小（允许前后偏移的步数）
     * @return 验证是否成功
     */
    public static boolean verifyTOTP(String key, String code, int timeWindow) {
        if (key == null || key.isEmpty() || code == null || code.isEmpty()) {
            return false;
        }

        long currentTime = System.currentTimeMillis() / 1000;
        long currentTimeStep = (currentTime - DEFAULT_START_TIME) / DEFAULT_TIME_STEP;

        // 检查当前时间步及其前后时间窗口内的步数
        for (long i = -timeWindow; i <= timeWindow; i++) {
            long timeStep = currentTimeStep + i;
            String steps = Long.toHexString(timeStep).toUpperCase();
            try {
                String totp = generateTOTP(key, steps);
                if (totp.equals(code)) {
                    return true;
                }
            } catch (Exception e) {
                // 忽略单个时间步的错误，继续验证其他步数
                continue;
            }
        }
        return false;
    }

    /**
     * 验证TOTP代码（使用默认时间窗口）
     */
    public static boolean verifyTOTP(String key, String code) {
        return verifyTOTP(key, code, DEFAULT_TIME_WINDOW);
    }

    /**
     * 主方法：测试TOTP算法实现
     * 使用RFC 6238中的测试向量验证算法正确性，并演示当前TOTP生成
     */
    public static void main(String[] args) {
        System.out.println("TOTP算法测试程序");
        System.out.println("================\n");

        // RFC 6238 测试向量
        String seed20 = "3132333435363738393031323334353637383930";      // 20字节密钥（SHA1）
        String seed32 = "3132333435363738393031323334353637383930313233343536373839303132"; // 32字节密钥（SHA256）
        String seed64 = "3132333435363738393031323334353637383930"
                + "3132333435363738393031323334353637383930"
                + "3132333435363738393031323334353637383930"
                + "31323334"; // 64字节密钥（SHA512）

        // 测试时间点（Unix时间戳）
        long[] testTime = {59L, 1111111109L, 1111111111L, 1234567890L, 2000000000L, 20000000000L};

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("UTC"));

        // 打印测试结果表格头
        System.out.println("RFC 6238 测试向量验证结果:");
        System.out.println("+---------------+-----------------------+------------------+----------+----------+");
        System.out.println("| 时间(秒)      |    UTC时间           |   T值(十六进制)  |  TOTP值  | 算法     |");
        System.out.println("+---------------+-----------------------+------------------+----------+----------+");

        // 测试每个时间点
        for (long timeValue : testTime) {
            long T = (timeValue - DEFAULT_START_TIME) / DEFAULT_TIME_STEP;
            String steps = Long.toHexString(T).toUpperCase();
            // 填充至16字符
            while (steps.length() < 16) {
                steps = "0" + steps;
            }

            String fmtTime = String.format("%1$-11s", timeValue);
            String utcTime = formatter.format(Instant.ofEpochSecond(timeValue));

            // 测试SHA1算法
            printResult(fmtTime, utcTime, steps, generateTOTP(seed20, steps, 8, HMAC_SHA1), "SHA1");

            // 测试SHA256算法
            printResult(fmtTime, utcTime, steps, generateTOTP(seed32, steps, 8, HMAC_SHA256), "SHA256");

            // 测试SHA512算法
            printResult(fmtTime, utcTime, steps, generateTOTP(seed64, steps, 8, HMAC_SHA512), "SHA512");

            System.out.println("+---------------+-----------------------+------------------+----------+----------+");
        }

        // 演示当前时间TOTP生成
        System.out.println("\n当前时间TOTP演示:");
        System.out.println("----------------");

        String currentTOTP = generateCurrentTOTP(seed20);
        System.out.println("共享密钥: " + seed20);
        System.out.println("当前TOTP: " + currentTOTP);

        // 验证演示
        boolean isValid = verifyTOTP(seed20, currentTOTP);
        System.out.println("TOTP验证: " + (isValid ? "通过" : "失败"));

        // 错误代码验证测试
        boolean isInvalid = verifyTOTP(seed20, "000000");
        System.out.println("错误代码验证: " + (isInvalid ? "通过" : "错误"));

        System.out.println("\n测试完成");
    }

    /**
     * 打印单行测试结果
     *
     * @param time 时间戳字符串
     * @param utcTime UTC时间字符串
     * @param steps 时间步十六进制值
     * @param totp TOTP值
     * @param mode 算法模式
     */
    private static void printResult(String time, String utcTime, String steps,
                                    String totp, String mode) {
        System.out.printf("| %s | %s | %s | %s | %-8s |%n",
                time, utcTime, steps, totp, mode);
    }
}
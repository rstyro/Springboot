package top.lrshuai.jwt.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import top.lrshuai.jwt.entity.RSA256Key;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;

/**
 * 参考地址：https://www.cnblogs.com/only-jlk/p/5960900.html
 */
public class CreateSecrteKey {

    public static final String KEY_ALGORITHM = "RSA";
    private static final String PUBLIC_KEY = "RSAPublicKey";
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    private static RSA256Key rsa256Key;

    //获得公钥
    public static String getPublicKey(Map<String, Object> keyMap) throws Exception {
        //获得map中的公钥对象 转为key对象
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        //byte[] publicKey = key.getEncoded();
        //编码返回字符串
        return encryptBASE64(key.getEncoded());
    }
    public static String getPublicKey(RSA256Key rsa256Key) throws Exception {
        //获得map中的公钥对象 转为key对象
        Key key = rsa256Key.getPublicKey();
        //byte[] publicKey = key.getEncoded();
        //编码返回字符串
        return encryptBASE64(key.getEncoded());
    }

    //获得私钥
    public static String getPrivateKey(Map<String, Object> keyMap) throws Exception {
        //获得map中的私钥对象 转为key对象
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        //byte[] privateKey = key.getEncoded();
        //编码返回字符串
        return encryptBASE64(key.getEncoded());
    }
    //获得私钥
    public static String getPrivateKey(RSA256Key rsa256Key) throws Exception {
        //获得map中的私钥对象 转为key对象
        Key key = rsa256Key.getPrivateKey();
        //byte[] privateKey = key.getEncoded();
        //编码返回字符串
        return encryptBASE64(key.getEncoded());
    }

    //解码返回byte
    public static byte[] decryptBASE64(String key) throws Exception {
        return (new BASE64Decoder()).decodeBuffer(key);
    }

    //编码返回字符串
    public static String encryptBASE64(byte[] key) throws Exception {
        return (new BASE64Encoder()).encodeBuffer(key);
    }

    //map对象中存放公私钥
    public static Map<String, Object> initKey() throws Exception {
        //  /** RSA算法要求有一个可信任的随机数源 */
        //获得对象 KeyPairGenerator 参数 RSA 1024个字节
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(1024);
        //通过对象 KeyPairGenerator 生成密匙对 KeyPair
        KeyPair keyPair = keyPairGen.generateKeyPair();

        //通过对象 KeyPair 获取RSA公私钥对象RSAPublicKey RSAPrivateKey
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        //公私钥对象存入map中
        Map<String, Object> keyMap = new HashMap<String, Object>(2);
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }

    /**
     * 获取公私钥
     * @return
     * @throws Exception
     */
    public static synchronized RSA256Key getRSA256Key() throws Exception {
        if(rsa256Key == null){
            synchronized (RSA256Key.class){
                if(rsa256Key == null) {
                    rsa256Key = new RSA256Key();
                    Map<String, Object> map = initKey();
                    rsa256Key.setPrivateKey((RSAPrivateKey) map.get(CreateSecrteKey.PRIVATE_KEY));
                    rsa256Key.setPublicKey((RSAPublicKey) map.get(CreateSecrteKey.PUBLIC_KEY));
                }
            }
        }
        return rsa256Key;
    }

    public static void main(String[] args) {
        Map<String, Object> keyMap;
        try {
            keyMap = initKey();
            String publicKey = getPublicKey(keyMap);
            System.out.println("公钥：\n"+publicKey);
            String privateKey = getPrivateKey(keyMap);
            System.out.println("私钥：\n"+privateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
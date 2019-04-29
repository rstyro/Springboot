package top.lrshuai.googlecheck.common;

public class CacheKey {
    /**
     * 登录生成的token key
     */
    public static final String TOKEN_KEY_LOGIN = "TOKEN_KEY_LOGIN-%s";

    /**
     * google验证保存的状态 key
     */
    public static final String TOKEN_KEY_GOOGLE = "TOKEN_KEY_GOOGLE-%s";


    /**
     * 注册的用户全部放redis缓存中
     */
    public static final String REGISTER_USER = "REGISTER_USER_%s";

    public static final String REGISTER_USER_KEY = "REGISTER_USER_*";
    public static final String TOKEN_KEY_LOGIN_KEY = "TOKEN_KEY_LOGIN*";

}

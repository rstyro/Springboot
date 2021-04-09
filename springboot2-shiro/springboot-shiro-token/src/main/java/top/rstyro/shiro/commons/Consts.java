package top.rstyro.shiro.commons;

/**
 * 常量类
 */
public class Consts {
    /**
     * 前端token令牌名称
     */
    public static final String TOKEN = "authority";
    /**
     * 存入redis的token 前缀
     */
    public static final String REDIS_TOKEN_KEY_PREFIX = "login_token:";
    /**
     * milliseconds：毫秒
     */
    public static final long TOKEN_TIME_OUT = 1000 * 60 * 1;
    /**
     * shiro 认证缓存 redis hash:大key
     */
    public static final String SHIRO_AUTHENTICATION_CACHE = "shiro_authentication_cache";
    /**
     * shiro 授权 缓存  redis hash:大key
     */
    public static final String SHIRO_AUTHORIZATION_CACHE = "shiro_authorization_cache";
}

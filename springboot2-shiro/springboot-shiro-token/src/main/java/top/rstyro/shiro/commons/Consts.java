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
     * 存入redis的token前缀，保存用户信息, key:前缀+token ，value：user 对象
     */
    public static final String REDIS_TOKEN_KEY_PREFIX = "login_token:";
    /**
     * 保存用户token  redis hash:大key，小key是userId
     */
    public static final String REDIS_USER_TOKEN = "login_user_token";
    /**
     * 旧的登录token,为了清除旧数据
     */
    public static final String OLD_TOKEN = "old_login_token";
    /**
     * milliseconds：毫秒
     */
    public static final long TOKEN_TIME_OUT = 1000 * 60 * 10;
    /**
     * shiro 认证缓存 redis hash:大key
     */
    public static final String SHIRO_AUTHENTICATION_CACHE = "shiro_authentication_cache";
    /**
     * shiro 授权 缓存  redis hash:大key
     */
    public static final String SHIRO_AUTHORIZATION_CACHE = "shiro_authorization_cache";
}

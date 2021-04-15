package top.rstyro.shiro.shiro.uitls;

import com.alibaba.fastjson.JSON;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import top.rstyro.shiro.commons.Consts;
import top.rstyro.shiro.shiro.session.RedisSessionDAO;
import top.rstyro.shiro.sys.entity.User;
import top.rstyro.shiro.utils.ApplicationContextUtils;

import java.util.concurrent.TimeUnit;

public class ShiroUtils {

    /** 私有构造器 **/
    private ShiroUtils(){}

    private static RedisSessionDAO redisSessionDAO = ApplicationContextUtils.getBean(RedisSessionDAO.class);


    /**
     * 获取当前用户session
     * @return
     */
    public static Session getSession() {
        return SecurityUtils.getSubject().getSession();
    }

    /**
     * 用户登出
     */
    public static void logout() {
        SecurityUtils.getSubject().logout();
    }

    /**
     * 获取当前用户token
     * @return
     */
    public static String getToken() {
        return (String) SecurityUtils.getSubject().getPrincipal();
    }

    public static User getUser(){
        return getUser(getToken());
    }

    public static User getUser(String token){
        Object obj = getRedisTemplate().opsForValue().get(Consts.REDIS_TOKEN_KEY_PREFIX + token);
        if(!ObjectUtils.isEmpty(obj)) {
            User user = JSON.parseObject(obj.toString(), User.class);
            return user;
        }
        return null;
    }

    /**
     * 登录保存用户信息到redis
     * @param currentUser user信息
     * @param newToken 新的token
     */
    public static void setLoginInfo(User currentUser,String newToken){
        RedisTemplate<String, Object> redisTemplate = getRedisTemplate();
        // 保存用户token,为了下一次登录之前获取，然后清除用户之前的旧数据
        redisTemplate.opsForHash().put(Consts.REDIS_USER_TOKEN,currentUser.getId().toString(),newToken);
        // 保存token到数据库，可以不用保存数据库直接redis也可以,这个token只是为了 realm校验用，而不返回给前端
        redisTemplate.opsForValue().set(Consts.REDIS_TOKEN_KEY_PREFIX+newToken,currentUser, Consts.TOKEN_TIME_OUT, TimeUnit.MILLISECONDS);
    }

    public static String getOldUserToken(Long userId){
        RedisTemplate<String, Object> redisTemplate = getRedisTemplate();
        Object o = redisTemplate.opsForHash().get(Consts.REDIS_USER_TOKEN, userId.toString());
        return ObjectUtils.isEmpty(o)?null:o.toString();
    }

    /**
     * 清除当前用户，之前登录的信息
     * @param oldUserToken
     */
    public static void clearOldUserInfo(String oldUserToken){
        if(!StringUtils.isEmpty(oldUserToken)){
            RedisTemplate<String, Object> redisTemplate = getRedisTemplate();
            System.out.println("oldToken1="+oldUserToken);
            System.out.println("oldToken2="+getSession().getAttribute(Consts.OLD_TOKEN));
            // 清除认证缓存
            redisTemplate.opsForHash().delete(Consts.SHIRO_AUTHENTICATION_CACHE,oldUserToken);
            // 清除授权缓存
            redisTemplate.opsForHash().delete(Consts.SHIRO_AUTHORIZATION_CACHE,oldUserToken);
            // 清除token
            redisTemplate.delete(Consts.REDIS_TOKEN_KEY_PREFIX+oldUserToken);
            // 清除session
            redisTemplate.opsForHash().delete(RedisSessionDAO.KEY_PREFIX,oldUserToken);
        }
    }

    public static RedisTemplate<String,Object> getRedisTemplate(){
        return (RedisTemplate) ApplicationContextUtils.getBean("redisTemplate",RedisTemplate.class);
    }
}

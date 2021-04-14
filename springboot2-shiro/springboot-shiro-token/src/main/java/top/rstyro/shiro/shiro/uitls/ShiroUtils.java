package top.rstyro.shiro.shiro.uitls;

import com.alibaba.fastjson.JSON;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authc.LogoutAware;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import top.rstyro.shiro.commons.Consts;
import top.rstyro.shiro.shiro.CustomerRealm;
import top.rstyro.shiro.shiro.session.RedisSessionDAO;
import top.rstyro.shiro.sys.entity.User;
import top.rstyro.shiro.utils.ApplicationContextUtils;

import java.util.Collection;
import java.util.Objects;
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

    /**
     * 删除用户缓存
     * @param username
     * @param isRemoveSession
     */
    public static void deleteCache(String username, boolean isRemoveSession){
        //从缓存中获取Session
        Session session = null;
        Collection<Session> sessions = redisSessionDAO.getActiveSessions();
        User User;
        Object attribute = null;
        for(Session sessionInfo : sessions){
            //遍历Session,找到该用户名称对应的Session
            attribute = sessionInfo.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
            if (attribute == null) {
                continue;
            }
            User = (User) ((SimplePrincipalCollection) attribute).getPrimaryPrincipal();
            if (User == null) {
                continue;
            }
            if (Objects.equals(User.getUsername(), username)) {
                session=sessionInfo;
                break;
            }
        }
        if (session == null||attribute == null) {
            return;
        }
        //删除session
        if (isRemoveSession) {
            redisSessionDAO.delete(session);
        }
        //删除Cache，在访问受限接口时会重新授权
        DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager) SecurityUtils.getSecurityManager();
        Authenticator authc = securityManager.getAuthenticator();
        ((LogoutAware) authc).onLogout((SimplePrincipalCollection) attribute);
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

    public static void setLoginInfo(User currentUser,String newToken){
        RedisTemplate<String, Object> redisTemplate = getRedisTemplate();
        // 保存用户token,为了清除用户之前的旧数据
        redisTemplate.opsForHash().put(Consts.REDIS_USER_TOKEN,currentUser.getId().toString(),newToken);
        // 保存token到数据库，可以不用保存数据库直接redis也可以,这个token只是为了 realm校验用，而不返回给前端
        redisTemplate.opsForValue().set(Consts.REDIS_TOKEN_KEY_PREFIX+newToken,currentUser, Consts.TOKEN_TIME_OUT, TimeUnit.MILLISECONDS);
    }

    public static String getOldUserToken(Long userId){
        RedisTemplate<String, Object> redisTemplate = getRedisTemplate();
        Object o = redisTemplate.opsForHash().get(Consts.REDIS_USER_TOKEN, userId.toString());
        return ObjectUtils.isEmpty(o)?null:o.toString();
    }

    public static final String DEFAULT_AUTHORIZATION_CACHE_SUFFIX = ".authorizationCache";

    public static void clearOldUserInfo(String oldUserToken){
        if(!StringUtils.isEmpty(oldUserToken)){
            RedisTemplate<String, Object> redisTemplate = getRedisTemplate();
            System.out.println("oldToken1="+oldUserToken);
            System.out.println("oldToken2="+getSession().getAttribute(Consts.OLD_TOKEN));
            redisTemplate.delete(Consts.REDIS_TOKEN_KEY_PREFIX+oldUserToken);
            redisTemplate.opsForHash().delete(Consts.SHIRO_AUTHENTICATION_CACHE,oldUserToken);
            redisTemplate.opsForHash().delete(RedisSessionDAO.KEY_PREFIX,oldUserToken);
            redisTemplate.opsForHash().delete(CustomerRealm.class.getName()+DEFAULT_AUTHORIZATION_CACHE_SUFFIX,oldUserToken);
        }
    }

    public static RedisTemplate<String,Object> getRedisTemplate(){
        return (RedisTemplate) ApplicationContextUtils.getBean("redisTemplate",RedisTemplate.class);
    }
}

package top.rstyro.shiro.shiro;

import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.rstyro.shiro.commons.Consts;
import top.rstyro.shiro.shiro.session.MySessionListener;
import top.rstyro.shiro.shiro.session.RedisSessionDAO;
import top.rstyro.shiro.shiro.session.ShiroSessionIdGenerator;
import top.rstyro.shiro.shiro.session.TokenSessionManager;

import javax.servlet.Filter;
import java.util.*;

/**
 * shiro 配置类
 */
@Configuration
public class ShiroConfig {

    //1.创建shiroFilter
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(DefaultWebSecurityManager defaultWebSecurityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //给filter设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);
        // 配置自己的shiro过滤器,命名为token
        Map<String, Filter> filter = new LinkedHashMap<>();
        filter.put("token", new CustomerFilter());
        shiroFilterFactoryBean.setFilters(filter);

        Map<String, String> map = new LinkedHashMap<>();
        map.put("/login", "anon");//anno 不拦截
        map.put("/logout", "anon");//anno 不拦截
        map.put("/**", "token"); // 其他请求通过 自定义的过滤器token

//        shiroFilterFactoryBean.setLoginUrl("/login");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
        return shiroFilterFactoryBean;
    }

    /**
     * 创建 默认的安全管理器
     */
    @Bean
    public DefaultWebSecurityManager getDefaultWebSecurityManager(CustomerRealm customerRealm) {
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        //给安全管理器设置
        defaultWebSecurityManager.setRealm(customerRealm);
        defaultWebSecurityManager.setSessionManager(sessionManager());

        return defaultWebSecurityManager;
    }


    /**
     * 自定义 realm
     * @return
     */
    @Bean
    public CustomerRealm customerRealm() {
        CustomerRealm customerRealm = new CustomerRealm();
        customerRealm.setCacheManager(new ShiroRedisCacheManager());
        customerRealm.setCachingEnabled(true);
        // 认证缓存
        customerRealm.setAuthenticationCachingEnabled(true);
        customerRealm.setAuthenticationCacheName(Consts.SHIRO_AUTHENTICATION_CACHE);
        // 授权缓存
        customerRealm.setAuthorizationCachingEnabled(true);
        customerRealm.setAuthorizationCacheName(Consts.SHIRO_AUTHORIZATION_CACHE);
        return customerRealm;
    }


    /**
     * 自定义 session 管理器
     * @return
     */
    @Bean
    public SessionManager sessionManager() {
        // 自定义session 从header 取 token 当作session会话
        TokenSessionManager sessionManager = new TokenSessionManager();
        //设置redisSessionDao
        sessionManager.setSessionDAO(redisSessionDAO());
        // 时间单位 毫秒
        sessionManager.setGlobalSessionTimeout(Consts.TOKEN_TIME_OUT);
        // 调用sessionDAO doDelete方法
        sessionManager.setDeleteInvalidSessions(true);

        //禁用cookie
        sessionManager.setSessionIdCookieEnabled(false);
        //禁用会话id重写
        sessionManager.setSessionIdUrlRewritingEnabled(false);

        // session监听
        Collection<SessionListener> listeners = new ArrayList<>();
        listeners.add(mySessionListener());
        sessionManager.setSessionListeners(listeners);

        return sessionManager;
    }


    @Bean
    public MySessionListener mySessionListener() {
        return new MySessionListener();
    }

    /**
     * SessionID生成器
     *
     */
    @Bean
    public ShiroSessionIdGenerator sessionIdGenerator(){
        return new ShiroSessionIdGenerator();
    }

    /**
     * 配置RedisSessionDAO
     * @return
     */
    @Bean
    public RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setSessionIdGenerator(sessionIdGenerator());
        return redisSessionDAO;
    }

    @Bean
    public ShiroRedisCacheManager shiroRedisCacheManager() {
        return new ShiroRedisCacheManager();
    }
}

package top.rstyro.shiro.shiro;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * shiro 配置类
 */
@Configuration
public class ShiroConfig {

    //1.创建shiroFilter
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(DefaultWebSecurityManager defaultWebSecurityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //给filter设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);
        Map<String,String> map = new HashMap<>();
        map.put("/toLogin","anon");//anno 不拦截
        map.put("/login","anon");//anno 不拦截
        map.put("/**","authc");

        //默认认证界面路径---当认证不通过时跳转
        shiroFilterFactoryBean.setLoginUrl("/toLogin");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
        return shiroFilterFactoryBean;
    }

    //2.创建安全管理器
    @Bean
    public DefaultWebSecurityManager getDefaultWebSecurityManager(CustomerRealm customerRealm){
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        //给安全管理器设置
        defaultWebSecurityManager.setRealm(customerRealm);
        defaultWebSecurityManager.setSessionManager(sessionManager());
        return defaultWebSecurityManager;
    }


    //3.创建自定义realm
    @Bean
    public CustomerRealm customerRealm(){
        CustomerRealm customerRealm = new CustomerRealm();
        customerRealm.setCacheManager(new ShiroRedisCacheManager());
        customerRealm.setCachingEnabled(true);
        // 认证
        customerRealm.setAuthenticationCachingEnabled(true);
        customerRealm.setAuthenticationCacheName("shiro_authentication_cache");
        // 授权
        customerRealm.setAuthorizationCachingEnabled(true);
        customerRealm.setAuthorizationCacheName("shiro_authorization_cache");
        return customerRealm;
    }

    /**
     * 配置ShiroDialect，用于thymeleaf和shiro标签配合使用
     */
    @Bean
    public ShiroDialect getShiroDialect(){
        return new ShiroDialect();
    }

    @Bean
    public SessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        //设置redisSessionDao
        sessionManager.setSessionDAO(redisSessionDAO());
        // 时间单位 毫秒
        sessionManager.setGlobalSessionTimeout(1000*60*3);
        // 调用sessionDAO doDelete方法
        sessionManager.setDeleteInvalidSessions(true);
        return sessionManager;
    }


    @Bean
    public RedisSessionDAO redisSessionDAO() {
        return new RedisSessionDAO();
    }

    @Bean
    public ShiroRedisCacheManager shiroRedisCacheManager(){
        return new ShiroRedisCacheManager();
    }
}

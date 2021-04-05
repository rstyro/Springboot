package top.rstyro.shiro.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
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
        //配置系统受限资源
        //配置系统公共资源
        Map<String,String> map = new HashMap<String,String>();

        map.put("/toLogin","anon");//anno 不拦截
        map.put("/login","anon");//anno 不拦截

        map.put("/user/add","perms[user:add]");
        map.put("/user/edit","perms[user:edit]");
        map.put("/user/del","perms[user:del]");
        // 下面的两个请求使用注解的方式
//        map.put("/test1","roles[test]");
//        map.put("/test2","perms[rstyro:test:query]");
        map.put("/**","authc");

        //默认认证界面路径---当认证不通过时跳转
        shiroFilterFactoryBean.setLoginUrl("/toLogin");
        shiroFilterFactoryBean.setUnauthorizedUrl("/noauth");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
        return shiroFilterFactoryBean;
    }

    //2.创建安全管理器
    @Bean
    public DefaultWebSecurityManager getDefaultWebSecurityManager(CustomerRealm customerRealm){
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        //给安全管理器设置
        defaultWebSecurityManager.setRealm(customerRealm);
        return defaultWebSecurityManager;
    }

    //3.创建自定义realm
    @Bean
    public CustomerRealm getCustomerRealm(){
        return new CustomerRealm();
    }

    /**
     * 配置ShiroDialect，用于thymeleaf和shiro标签配合使用
     */
    @Bean
    public ShiroDialect getShiroDialect(){
        return new ShiroDialect();
    }

}

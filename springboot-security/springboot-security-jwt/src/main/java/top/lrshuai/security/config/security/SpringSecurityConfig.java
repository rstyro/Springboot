package top.lrshuai.security.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import top.lrshuai.security.handler.AccessDeny;
import top.lrshuai.security.handler.AnonymousAuthenticationEntryPoint;
import top.lrshuai.security.handler.AuthenticationLogout;

@EnableWebSecurity
//开启权限注解,默认是关闭的
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    //自定义未登录返回
    private AnonymousAuthenticationEntryPoint anonymousAuthenticationEntryPoint;
    //自定义注销返回
    private AuthenticationLogout authenticationLogout;
    //自定义无权访问返回
    private AccessDeny accessDeny;
    // 自定义用户认证service
    private SecurityUserService securityUserService;

    @Autowired
    public void setAnonymousAuthenticationEntryPoint(AnonymousAuthenticationEntryPoint anonymousAuthenticationEntryPoint) {
        this.anonymousAuthenticationEntryPoint = anonymousAuthenticationEntryPoint;
    }

    @Autowired
    public void setAuthenticationLogout(AuthenticationLogout authenticationLogout) {
        this.authenticationLogout = authenticationLogout;
    }
    @Autowired
    public void setAccessDeny(AccessDeny accessDeny) {
        this.accessDeny = accessDeny;
    }
    @Autowired
    public void setSecurityUserService(SecurityUserService securityUserService) {
        this.securityUserService = securityUserService;
    }

    //加密方式
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //认证
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 自定义认证逻辑
        auth.userDetailsService(securityUserService).passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(WebSecurity web) {
        // 放行 swagger 相关路径
        String[] authWhiteList = {
                "/swagger-ui.html",
                "/webjars/**",
                "/swagger-resources/**",
                "/v2/**",
                "/csrf",

                // other
                "/css/**",
                "/js/**",
                "/html/**",
                "/instances",
                "/favicon.ico"
        };
        //对于在header里面增加token等类似情况，放行所有OPTIONS请求。
        web.ignoring()
                .antMatchers(HttpMethod.OPTIONS, "/**")
                // 可以直接访问的静态数据或接口
                .antMatchers(authWhiteList);
    }


    //授权
    @Override
    protected void configure(HttpSecurity http) throws Exception {
            http
                .authorizeRequests()// 授权
                .antMatchers("/index/**").anonymous()// 匿名用户权限
                .antMatchers("/api/**").hasRole("USER")//普通用户权限
                .antMatchers("/login").permitAll()
                //其他的需要授权后访问
                .anyRequest().authenticated()
                .and()// 异常
                .exceptionHandling()
                .accessDeniedHandler(accessDeny)//授权异常处理
                .authenticationEntryPoint(anonymousAuthenticationEntryPoint)// 认证异常处理
                .and()
                .logout()
                .logoutSuccessHandler(authenticationLogout)
                .and()
                .addFilterBefore(new JWTLoginFilter("/login",authenticationManager()),UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new SecurityAuthTokenFilter(authenticationManager()),UsernamePasswordAuthenticationFilter.class)
                .sessionManagement()
                // 设置Session的创建策略为：Spring Security不创建HttpSession
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf().disable();// 关闭 csrf
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

}

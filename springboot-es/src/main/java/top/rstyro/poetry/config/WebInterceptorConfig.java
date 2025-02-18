package top.rstyro.poetry.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.rstyro.poetry.interceptor.ContextInterceptor;

@Configuration
public class WebInterceptorConfig implements WebMvcConfigurer {
    private ContextInterceptor contextInterceptor;

    @Autowired
    public void setContextInterceptor(ContextInterceptor contextInterceptor) {
        this.contextInterceptor = contextInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(contextInterceptor).addPathPatterns("/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")// 允许跨域的域名，可以用*表示允许任何域名使用
                .allowedMethods("*")// 允许任何方法（post、get等）
                .allowCredentials(true)// 带上cookie信息
                .maxAge(3600) // 表明在3600秒内，不需要再发送预检验请求，可以缓存该结果
                .allowedHeaders("*");
    }
}

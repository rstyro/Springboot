package top.lrshuai.timer.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorAdapter implements WebMvcConfigurer {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// 这是要被当成静态文件的！
		// 第一个方法设置访问路径前缀，第二个方法设置资源路径
		registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		log.info("拦截在这里写");
	}
}

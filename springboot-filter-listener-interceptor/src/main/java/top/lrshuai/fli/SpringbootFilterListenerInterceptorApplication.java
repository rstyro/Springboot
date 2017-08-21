package top.lrshuai.fli;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * @ServletComponentScan 扫描我们自定义的servlet
 * @author tyro
 *
 */
@ServletComponentScan
@SpringBootApplication
public class SpringbootFilterListenerInterceptorApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootFilterListenerInterceptorApplication.class, args);
	}
}

package top.lrshuai.md;

import javax.servlet.MultipartConfigElement;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

	/**
	 * 保存图片的根目录
	 */
	@Value("${upload.root.folder}")
	public String locatFolder;
	
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	/**
	 * 这个bean是为了自定义上传路径
	 * @return
	 */
	@Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setLocation(locatFolder);
        return factory.createMultipartConfig();
    }
}

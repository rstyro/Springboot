package top.lrshuai.SpringBootmultisource;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@MapperScan("top.lrshuai.SpringBootmultisource.mapper")
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class SpringBootMultiSourceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootMultiSourceApplication.class, args);
	}
}

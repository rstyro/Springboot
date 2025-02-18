package top.lrshuai.sqlite;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(value = "top.lrshuai.sqlite.*.mapper*")
@SpringBootApplication
public class SpringbootSqliteApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootSqliteApplication.class, args);
    }

}

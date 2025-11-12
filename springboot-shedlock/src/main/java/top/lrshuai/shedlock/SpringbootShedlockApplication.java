package top.lrshuai.shedlock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SpringbootShedlockApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootShedlockApplication.class, args);
    }

}

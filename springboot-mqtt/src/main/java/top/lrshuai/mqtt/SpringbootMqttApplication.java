package top.lrshuai.mqtt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SpringbootMqttApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootMqttApplication.class, args);
    }

}

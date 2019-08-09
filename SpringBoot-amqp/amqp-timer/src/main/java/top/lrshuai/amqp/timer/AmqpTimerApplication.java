package top.lrshuai.amqp.timer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"top.lrshuai.amqp"})
@EnableScheduling
public class AmqpTimerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AmqpTimerApplication.class, args);
    }

}

package top.lrshuai.amqp.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"top.lrshuai.amqp"})
public class AmqpConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AmqpConsumerApplication.class, args);
    }

}

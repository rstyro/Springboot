package top.lrshuai.amqp.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"top.lrshuai.amqp"})
public class AmqpProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(AmqpProviderApplication.class, args);
    }

}

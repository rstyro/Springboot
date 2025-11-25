package top.lrshuai.helloword;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	private static Logger log = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		String name="rstyro";
		log.debug("------------debug--------------{}", name);
		log.info("------------info--------------{}", name);
		log.warn("------------warn--------------{}", name);
		log.error("------------error--------------{}", name);
	}
}

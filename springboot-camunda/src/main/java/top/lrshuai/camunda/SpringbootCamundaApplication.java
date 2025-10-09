package top.lrshuai.camunda;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

@Slf4j
@SpringBootApplication
public class SpringbootCamundaApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext application = SpringApplication.run(SpringbootCamundaApplication.class, args);
        Environment env = application.getEnvironment();
//        String ip = NetUtil.getLocalhostStr();
        String ip = "127.0.0.1";
        String port = env.getProperty("server.port");
        String contextPath = env.getProperty("server.servlet.context-path","");
        String banner = """
        \n\t
        ----------------------------------------------------------
        SpringbootCamundaApplication is running! Access URLs:
        Local: \t\thttp://localhost:%s%s/
        External: \thttp://%s:%s%s/
        ----------------------------------------------------------
        """.formatted(port, contextPath, ip, port, contextPath);
        log.info(banner);
    }

}

package top.lrshuai.jasypt.controller;

import jakarta.annotation.Resource;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jasypt")
public class JasyptController {

    @Resource
    private Environment environment;

    /**
     * 测试获取配置内容
     * @param key 配置的key
     * @return key的内容
     */
    @GetMapping("/getConfig")
    public Object getConfig(String key){
        // 动态获取配置文件的key
        String value = environment.getProperty(key);
        return String.format("配置项 %s 的值为: %s", key, value);
    }
}

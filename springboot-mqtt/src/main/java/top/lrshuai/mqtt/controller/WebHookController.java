package top.lrshuai.mqtt.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * EMQX webhook配置
 */
@Slf4j
@RestController
@RequestMapping("/webhook")
@RequiredArgsConstructor
public class WebHookController {

    /**
     * 接收消息发布的
     * @param data 参数
     * @return
     */
    @PostMapping("/publishHook")
    public Object publishHook(@RequestBody Map<String, Object> data) {
        log.info("webhook-收到发布消息data={}", data);
        String payload = (String) data.get("payload");
        return "返回值随便返回";
    }

    /**
     * 事件监听的
     * @param data 参数
     * @return 随便
     */
    @PostMapping("/eventHook")
    public Object eventHook(@RequestBody Map<String, Object> data) {
        log.info("webhook-收到事件data={}", data);
        return "ok";
    }

}

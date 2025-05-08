package top.lrshuai.mqtt.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import top.lrshuai.mqtt.config.MqttClientManager;
import top.lrshuai.mqtt.config.MqttClientProperties;

import java.util.Map;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final MqttClientManager mqttClientManager;
    private final MqttClientProperties mqttClientProperties;

    @PostMapping("/sendMessage")
    public Object sendMessage(@RequestBody Map<String, Object> data) {
        String topic = (String) data.get("topic");
        if(Objects.isNull(topic)) {
            topic = "test/topic";
        }
        String finalTopic = topic;
        mqttClientProperties.getClients().forEach((clientName, v)->{
            try {
                mqttClientManager.publish(clientName, finalTopic,data,1);
            }catch (Exception e){
                log.error(e.getMessage(),e);
            }
        });
        return data;
    }
}

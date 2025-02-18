package com.lrshuai.springbootkafka.controller;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.UUID;

@RequestMapping("/test")
@RestController
public class TestController {

    final static String TOPIC = "test";
    @Resource
    private KafkaTemplate<String,String> kafkaTemplate;

    @GetMapping("/send")
    public String sendMessage(String message) {
        String key = UUID.randomUUID().toString();
        kafkaTemplate.send(TOPIC, key,message);
        return key;
    }
}

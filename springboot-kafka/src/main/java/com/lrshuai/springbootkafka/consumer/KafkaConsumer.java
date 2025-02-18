package com.lrshuai.springbootkafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaConsumer {

    @KafkaListener(topics = {"test"},groupId = "my-Group")
    public void consumer(ConsumerRecord<String,Object> consumerRecord){
        log.info("接收消息：key={},value={},partition={}",consumerRecord.key(),consumerRecord.value(),consumerRecord.partition());
    }
}

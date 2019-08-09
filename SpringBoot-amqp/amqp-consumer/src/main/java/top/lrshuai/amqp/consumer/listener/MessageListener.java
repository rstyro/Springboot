package top.lrshuai.amqp.consumer.listener;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import top.lrshuai.amqp.commons.enums.ExchangeEnum;
import top.lrshuai.amqp.commons.enums.QueueEnum;
import top.lrshuai.amqp.commons.sys.entity.Message;

import java.util.Map;

@Component
public class MessageListener {

    /**
     * @RabbitListener 消息监听，可配置交换机、队列、路由key
     * 该注解会创建队列和交互机 并建立绑定关系
     * @RabbitHandler 标识此方法如果有消息过来，消费者要调用这个方法
     * @Payload 消息体
     * @Headers 消息头
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "test.message.queue", declare = "true"),
            exchange = @Exchange(name = "test.message.exchange", declare = "true", type = "topic"),
            key = "#.message"
    ))
    @RabbitHandler
    public void onMessage(@Payload Message message, @Headers Map<String, Object> headers,
                          Channel channel) throws Exception {
        //消费者操作
        try {
            System.out.println("------收到消息，开始消费------");
            System.out.println("内容：" + JSON.toJSONString(message));

            Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
            //现在是手动确认消息 ACK
            channel.basicAck(deliveryTag, false);
        } finally {
            channel.close();
        }
    }
}

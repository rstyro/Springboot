package top.lrshuai.amqp.provider.producer;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.lrshuai.amqp.commons.sys.entity.Message;

@Component
public class MessageSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(Message message) {

        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(message.getMessageId());
        rabbitTemplate.convertAndSend("message.exchanges",//exchange
                "message.test",//routingKey
                message,//消息体内容
                correlationData); //消息唯一id
    }
}

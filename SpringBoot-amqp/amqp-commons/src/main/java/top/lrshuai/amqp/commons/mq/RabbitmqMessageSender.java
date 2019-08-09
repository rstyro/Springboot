package top.lrshuai.amqp.commons.mq;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.lrshuai.amqp.commons.constant.Consts;
import top.lrshuai.amqp.commons.enums.ExchangeEnum;
import top.lrshuai.amqp.commons.enums.QueueEnum;
import top.lrshuai.amqp.commons.sys.entity.Message;
import top.lrshuai.amqp.commons.sys.entity.MessageLog;
import top.lrshuai.amqp.commons.sys.service.IMessageLogService;

import java.time.LocalDateTime;

@Component
@Slf4j
public class RabbitmqMessageSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private IMessageLogService messageLogService;

    /**
     * Broker应答后，会调用该方法区获取应答结果
     */
    final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {
            try {
                log.info("correlationData：" + correlationData);
                String messageId = correlationData.getId();
                log.info("消息确认返回值：" + ack);
                if (ack) {
                    //如果返回成功，则进行更新
                    System.out.println("messageId=" + messageId);
                    MessageLog messageLog = messageLogService.getOne(new LambdaQueryWrapper<MessageLog>().eq(MessageLog::getMessageId, messageId));
                    if(messageLog != null){
                        messageLog.setStatus(Consts.MESSAGE_STATUS_SUCCESS).setUpdateTime(LocalDateTime.now());
                        messageLogService.updateById(messageLog);
                    }
                } else {
                    //失败进行操作：根据具体失败原因选择重试或补偿等手段
                    log.error("异常处理,返回结果：" + cause);
                }
            }catch (Exception e){
                log.error(e.getMessage(),e);
            }

        }
    };

    /**
     * 发送消息方法调用: 构建自定义对象消息
     *
     * @throws Exception
     */
    public synchronized void sendMessage(Message message) throws Exception {
        // 通过实现 ConfirmCallback 接口，消息发送到 Broker 后触发回调，确认消息是否到达 Broker 服务器，也就是只确认是否正确到达 Exchange 中
        rabbitTemplate.setConfirmCallback(confirmCallback);
        //消息唯一ID
        CorrelationData correlationData = new CorrelationData(message.getMessageId());
        rabbitTemplate.convertAndSend(ExchangeEnum.TEST_MESSAGE.getValue(), QueueEnum.TEST_MESSAGE.getRoutingKey(), message, correlationData);
        System.out.println("发送成功，消息="+ JSON.toJSONString(message));
    }

}

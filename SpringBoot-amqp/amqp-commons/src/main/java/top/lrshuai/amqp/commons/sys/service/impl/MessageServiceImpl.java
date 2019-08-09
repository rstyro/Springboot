package top.lrshuai.amqp.commons.sys.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.lrshuai.amqp.commons.constant.Consts;
import top.lrshuai.amqp.commons.constant.Result;
import top.lrshuai.amqp.commons.mq.RabbitmqMessageSender;
import top.lrshuai.amqp.commons.sys.entity.Message;
import top.lrshuai.amqp.commons.sys.entity.MessageLog;
import top.lrshuai.amqp.commons.sys.mapper.MessageMapper;
import top.lrshuai.amqp.commons.sys.service.IMessageLogService;
import top.lrshuai.amqp.commons.sys.service.IMessageService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author rstyro
 * @since 2019-08-09
 */
@Service
@Transactional
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements IMessageService {

    @Autowired
    private IMessageLogService messageLogService;

    @Autowired
    private RabbitmqMessageSender rabbitmqMessageSender;

    @Override
    public Result sendMessage(Message message) throws Exception {
        message.setMessageId(UUID.randomUUID().toString());
        this.save(message);
        MessageLog messageLog = new MessageLog();
        messageLog.setMessageId(message.getMessageId())
                .setMessage(JSON.toJSONString(message))
                .setIsDel(0)
                .setStatus(Consts.MESSAGE_STATUS_SENDING)
                .setTryCount(0)
                .setCreateTime(LocalDateTime.now())
                .setNextRetry(LocalDateTime.now().plus(Consts.TIME_OUT, ChronoUnit.MINUTES));
        messageLogService.save(messageLog);
        rabbitmqMessageSender.sendMessage(message);
        return Result.ok();
    }
}

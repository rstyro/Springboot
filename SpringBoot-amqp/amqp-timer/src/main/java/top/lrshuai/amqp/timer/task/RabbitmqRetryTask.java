package top.lrshuai.amqp.timer.task;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.lrshuai.amqp.commons.constant.Consts;
import top.lrshuai.amqp.commons.mq.RabbitmqMessageSender;
import top.lrshuai.amqp.commons.sys.entity.Message;
import top.lrshuai.amqp.commons.sys.entity.MessageLog;
import top.lrshuai.amqp.commons.sys.service.IMessageLogService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class RabbitmqRetryTask {
    @Autowired
    private RabbitmqMessageSender rabbitmqMessageSender;

    @Autowired
    private IMessageLogService messageLogService;


    /**
     * initialDelay 容器启动后,延迟 多少秒启动
     * fixedDelay是以上一次方法执行完开始算起
     */
    @Scheduled(initialDelay = 5000, fixedDelay = 10000)
    public void reSend() {
        System.out.println("-----------定时任务开始-----------"+LocalDateTime.now());
        //pull status = 0 and timeout message

        List<MessageLog> list = messageLogService.list(new LambdaQueryWrapper<MessageLog>()
                .lt(MessageLog::getNextRetry, LocalDateTime.now())
                .eq(MessageLog::getStatus, Consts.MESSAGE_STATUS_SENDING)
        );
        list.forEach(messageLog -> {
            //投递三次以上的消息
            if (messageLog.getTryCount() >= 3) {
                //update fail message
                messageLogService.updateById(messageLog.setStatus(Consts.MESSAGE_STATUS_FAILED).setUpdateTime(LocalDateTime.now()));
            } else {
                // resend
                messageLogService.updateById(messageLog
                        .setNextRetry(LocalDateTime.now().plus(Consts.TIME_OUT, ChronoUnit.MINUTES))
                        .setTryCount(messageLog.getTryCount()+1)
                        .setUpdateTime(LocalDateTime.now()));
                Message message = JSON.parseObject(messageLog.getMessage(), Message.class);
                try {
                    rabbitmqMessageSender.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("-----------异常处理-----------");
                }
            }
        });
    }
}

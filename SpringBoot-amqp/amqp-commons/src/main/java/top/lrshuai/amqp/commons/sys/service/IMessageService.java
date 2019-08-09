package top.lrshuai.amqp.commons.sys.service;

import org.springframework.stereotype.Component;
import top.lrshuai.amqp.commons.constant.Result;
import top.lrshuai.amqp.commons.sys.entity.Message;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author rstyro
 * @since 2019-08-09
 */
@Component
public interface IMessageService extends IService<Message> {

    public Result sendMessage(Message message) throws Exception;
}

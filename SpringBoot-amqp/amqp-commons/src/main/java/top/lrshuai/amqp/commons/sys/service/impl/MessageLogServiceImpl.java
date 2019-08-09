package top.lrshuai.amqp.commons.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.lrshuai.amqp.commons.sys.entity.MessageLog;
import top.lrshuai.amqp.commons.sys.mapper.MessageLogMapper;
import top.lrshuai.amqp.commons.sys.service.IMessageLogService;

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
public class MessageLogServiceImpl extends ServiceImpl<MessageLogMapper, MessageLog> implements IMessageLogService {

}

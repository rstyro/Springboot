package top.lrshuai.amqp.provider.sys.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.lrshuai.amqp.commons.base.BaseController;
import top.lrshuai.amqp.commons.constant.Result;
import top.lrshuai.amqp.commons.sys.entity.Message;
import top.lrshuai.amqp.commons.sys.service.IMessageService;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author rstyro
 * @since 2019-08-09
 */
@RestController
@RequestMapping("/sys/message")
public class MessageController extends BaseController {

    @Autowired
    private IMessageService messageService;

    @GetMapping("/test")
    public Object test(@RequestParam(value = "pageNo",defaultValue = "1") Integer pageNo, @RequestParam(value = "pageSize",defaultValue = "10")Integer pageSize) {
        return messageService.page(new Page<Message>(pageNo,pageSize));
    }

    /**
     * 发送消息
     * @param message
     * @return
     * @throws Exception
     */
    @GetMapping("/send")
    public Result sendMessage(Message message) throws Exception {
        return messageService.sendMessage(message);
    }
}

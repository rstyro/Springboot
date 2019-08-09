package top.lrshuai.amqp.provider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.lrshuai.amqp.commons.sys.entity.Message;
import top.lrshuai.amqp.provider.producer.MessageSender;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AmqpProviderApplicationTests {

    @Autowired
    private MessageSender messageSender;

    @Test
    public void testSendr() {
        messageSender.send(new Message().setId(1l).setContent("test").setMessageId("111"));
    }

}

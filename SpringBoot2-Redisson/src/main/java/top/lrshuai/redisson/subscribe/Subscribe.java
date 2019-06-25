package top.lrshuai.redisson.subscribe;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class Subscribe {

    @Autowired
    private RedissonClient redissonClient;

    @PostConstruct
    public void subscribe(){
        RTopic rTopic = redissonClient.getTopic(Consts.TopicName);
        rTopic.addListener(MyObjectDTO.class, new MessageListener<MyObjectDTO>() {
            // 接受订阅的消息
            @Override
            public void onMessage(CharSequence charSequence, MyObjectDTO myObjectDTO) {
                log.info("接受到消息主题={}，内容={}",charSequence,myObjectDTO);
                System.out.println("传输的数据为="+myObjectDTO);
            }
        });
    }
}

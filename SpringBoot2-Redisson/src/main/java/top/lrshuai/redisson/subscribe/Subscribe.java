package top.lrshuai.redisson.subscribe;

import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;

public class Subscribe {

    @Autowired
    private RedissonClient redissonClient;

    public void subscribe(){
        RTopic topic = redissonClient.getTopic("anyTopic");
        topic.addListener(MyObjectDTO.class, new MessageListener<MyObjectDTO>() {
            @Override
            public void onMessage(CharSequence charSequence, MyObjectDTO myObjectDTO) {
                System.out.println("charSequence="+charSequence);
                System.out.println("myObjectDTO="+myObjectDTO);
            }
        });
    }
}

package top.lrshuai.mq.mq;

import com.alibaba.fastjson.JSON;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import top.lrshuai.mq.comment.Const;
import top.lrshuai.mq.test.entity.AccountPlan;
import top.lrshuai.mq.test.entity.UserAccount;
import top.lrshuai.mq.test.entity.UserAccountDetail;
import top.lrshuai.mq.test.service.IAccountPlanService;
import top.lrshuai.mq.test.service.IUserAccountDetailService;
import top.lrshuai.mq.test.service.IUserAccountService;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 描述: 消息消费者
 **/
@Component
public class PlanConsumer {

    /**
     * 消费者的组名
     */
    @Value("${apache.rocketmq.consumer.PushConsumer}")
    private String consumerGroup;

    /**
     * NameServer地址
     */
    @Value("${apache.rocketmq.namesrvAddr}")
    private String namesrvAddr;

    @Autowired
    private IAccountPlanService accountPlanService;

    @Autowired
    private IUserAccountService userAccountService;

    @Autowired
    private IUserAccountDetailService userAccountDetailService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostConstruct
    public void defaultMQPushConsumer() {
        //消费者的组名
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(consumerGroup);
        //指定NameServer地址，多个地址以 ; 隔开
        consumer.setNamesrvAddr(namesrvAddr);
        try {
            //订阅PLAN_TOPIC，下所有消息
            consumer.subscribe(Const.PLAN_TOPIC, "*");
            //设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费
            //如果非第一次启动，那么按照上次消费的位置继续消费
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            /**
             * 设置一次消费消息的条数，当前为1条
             */
//            consumer.setConsumeMessageBatchMaxSize(1);
            consumer.registerMessageListener((MessageListenerConcurrently) (list, context) -> {
                MessageExt tempMsg= null;

                try {
                    System.out.println("=========================================list.size="+list.size());
                    for (MessageExt msg : list) {
                        System.out.println("messageExt: " + msg);//输出消息内容
                        String messageBody = new String(msg.getBody(), "utf-8");
                        System.out.println("消费响应：Msg: " + msg.getMsgId() + ",msgBody: " + messageBody);//输出消息内容
                        tempMsg=msg;
                        String topic = msg.getTopic();
                        //Message Body
                        AccountPlan plan = JSON.parseObject(new String(msg.getBody(), "utf-8"), AccountPlan.class);
                        String tags = msg.getTags();
                        String keys = msg.getKeys();
                        System.out.println("tags==="+tags);
                        System.out.println("Consumer服务收到消息, keys : " + keys + ", body : " + new String(msg.getBody(), "utf-8"));
                        plan.setIntoAccount(1);
                        accountPlanService.saveOrUpdate(plan);
                        UserAccount userAccount = new UserAccount();
                        userAccount.setAddress("test");
                        userAccountService.save(userAccount);
                        UserAccountDetail detail = new UserAccountDetail();
                        detail.setAmount(plan.getOperateAmount());
                        detail.setCode(plan.getOperateCode());
                        userAccountDetailService.save(detail);
                        logger.info("消费成功：msgId={}，plan={}",msg.getMsgId(),plan);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    //重试次数为3情况
                    if(tempMsg.getReconsumeTimes() == 3){
                        //记录日志
                        logger.error("msg consume error by:{}",tempMsg);
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    }
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER; //稍后再试
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS; //消费成功
            });
            consumer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

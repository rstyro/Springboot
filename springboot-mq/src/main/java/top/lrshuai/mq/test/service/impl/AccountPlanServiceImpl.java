package top.lrshuai.mq.test.service.impl;

import com.alibaba.fastjson.JSON;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import top.lrshuai.mq.comment.Const;
import top.lrshuai.mq.mq.PlanProducer;
import top.lrshuai.mq.mq.TransactionExecuterImpl;
import top.lrshuai.mq.test.entity.AccountPlan;
import top.lrshuai.mq.test.entity.PlanTopic;
import top.lrshuai.mq.test.mapper.AccountPlanMapper;
import top.lrshuai.mq.test.service.IAccountPlanService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * 入账计划表 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2018-11-24
 */
@Service
public class AccountPlanServiceImpl extends ServiceImpl<AccountPlanMapper, AccountPlan> implements IAccountPlanService {

    @Autowired
    private PlanProducer planProducer;

    @Autowired
    private TransactionExecuterImpl transactionExecuterImpl;

    @Override
    public void saveAccountPlan(AccountPlan plan) throws Exception {
        plan.setCreateTime(new Date());
        System.out.println("!!!!!!!!!!!!!plan="+plan);
        this.save(plan);
        sendToMQ(plan,"plan");
    }

    public void sendToMQ(Object object,String tags) throws Exception {
        //构造消息数据
        Message message = new Message();
        //主题
        message.setTopic(Const.PLAN_TOPIC);
        message.setTags(tags);
        //key
        String uuid = UUID.randomUUID().toString();
        message.setKeys(uuid);
        message.setBody(JSON.toJSONString(object).getBytes());
        //添加参数
        Map<String, Object> transactionMapArgs = new HashMap<String, Object>();
        this.planProducer.sendTransactionMessage(message, this.transactionExecuterImpl, transactionMapArgs);
    }

}

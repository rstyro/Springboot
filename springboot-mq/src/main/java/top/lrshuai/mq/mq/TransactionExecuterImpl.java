package top.lrshuai.mq.mq;

import com.alibaba.fastjson.JSON;
import org.apache.rocketmq.client.producer.LocalTransactionExecuter;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.lrshuai.mq.test.entity.AccountPlan;
import top.lrshuai.mq.test.service.IAccountPlanService;

import java.util.Map;


/**
 * 执行本地事务，由客户端回调
 */

@Component("transactionExecuterImpl")
public class TransactionExecuterImpl implements LocalTransactionExecuter {
   
	@Autowired
	private IAccountPlanService accountPlanService;
	
	public LocalTransactionState executeLocalTransactionBranch(Message msg, Object arg) {
		try {
			//Message Body
			Object plan = JSON.parseObject(new String(msg.getBody(), "utf-8"), Object.class);
			//Transaction MapArgs
			Map<String, Object> mapArgs = (Map<String, Object>) arg;

			// --------------------IN PUT---------------------- //
			System.out.println("message plan = " + plan);
			System.out.println("message mapArgs = " + mapArgs);
			System.out.println("message tag = " + msg.getTags());
			// --------------------IN PUT---------------------- //

//			accountPlanService.save(plan);
			//成功通知MQ消息变更 该消息变为：<确认发送>
			return LocalTransactionState.COMMIT_MESSAGE;
			
			//return LocalTransactionState.UNKNOW;
			
		} catch (Exception e) {
			e.printStackTrace();
			//失败则不通知MQ 该消息一直处于：<暂缓发送>
			return LocalTransactionState.ROLLBACK_MESSAGE;
			
		}
		
	}
}
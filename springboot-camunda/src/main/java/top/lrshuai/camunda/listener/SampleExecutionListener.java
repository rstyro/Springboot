package top.lrshuai.camunda.listener;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

/**
 * 简单监听器
 */
@Component
public class SampleExecutionListener implements ExecutionListener {

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        String eventName = execution.getEventName();
        if (eventName.equals("start")) {
            System.out.println("Process started: " + execution.getProcessInstanceId());
        }
    }
}

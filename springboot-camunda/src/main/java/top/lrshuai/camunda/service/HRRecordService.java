package top.lrshuai.camunda.service;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class HRRecordService implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String processInstanceId = execution.getProcessInstanceId();
        String applicant = (String) execution.getVariable("applicant");
        Double leaveDays = (Double) execution.getVariable("leaveDays");
        String leaveType = (String) execution.getVariable("leaveType");

        log.info("人事备案 - 流程实例: {}, 申请人: {}, 请假类型: {}, 天数: {}",
                processInstanceId, applicant, leaveType, leaveDays);

        // 这里可以添加实际的HR系统集成逻辑
        // 如更新考勤系统、记录请假记录等

        execution.setVariable("hrRecorded", true);
        execution.setVariable("recordTime", LocalDateTime.now());
    }
}

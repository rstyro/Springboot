package top.lrshuai.camunda.service;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NotificationService implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String applicant = (String) execution.getVariable("applicant");
        Boolean approved = false;
        String comment = "";

        // 判断是经理审批还是总监审批的拒绝
        if (execution.hasVariable("managerApproved")) {
            approved = (Boolean) execution.getVariable("managerApproved");
            comment = (String) execution.getVariable("managerComment");
        } else if (execution.hasVariable("directorApproved")) {
            approved = (Boolean) execution.getVariable("directorApproved");
            comment = (String) execution.getVariable("directorComment");
        }

        if (!approved) {
            log.info("发送通知 - 申请人: {}, 审批结果: 拒绝, 原因: {}", applicant, comment);
            // 这里可以集成邮件、短信、企业微信等通知方式
        }
    }
}

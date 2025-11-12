package top.lrshuai.camunda.controller;

import jakarta.annotation.Resource;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.history.HistoricActivityInstance;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.lrshuai.camunda.dto.LeaveApplicationDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/leave")
public class LeaveProcessController {

    @Resource
    private RuntimeService runtimeService;

    @Resource
    private TaskService taskService;

    @Resource
    private HistoryService historyService;

    @Resource
    private IdentityService identityService;

    /**
     * 启动请假流程
     */
    @PostMapping("/start")
    public ResponseEntity<Map<String, Object>> startLeaveProcess(@RequestBody LeaveApplicationDto application) {
        try {
            // 设置流程启动者
            identityService.setAuthenticatedUserId(application.getApplicant());

            Map<String, Object> variables = new HashMap<>();
            variables.put("applicant", application.getApplicant());
            variables.put("leaveType", application.getLeaveType());
            variables.put("startDate", application.getStartDate());
            variables.put("endDate", application.getEndDate());
            variables.put("leaveDays", application.getLeaveDays());
            variables.put("reason", application.getReason());
            // 设置审批人（实际项目中可以从用户服务获取）
            variables.put("departmentManager", getDepartment(application.getApplicant()));
            variables.put("director", "director_company");

            var instance = runtimeService.startProcessInstanceByKey("LeaveProcess", variables);

            Map<String, Object> result = new HashMap<>();
            result.put("processInstanceId", instance.getId());
            result.put("message", "请假流程已启动");

            return ResponseEntity.ok(result);

        } finally {
            identityService.clearAuthentication();
        }
    }

    /**
     * 获取用户待办任务
     * @param userId 用户
     */
    @GetMapping("/tasks/{userId}")
    public ResponseEntity<List<Map<String, Object>>> getUserTasks(@PathVariable String userId) {
        List<Task> tasks = taskService.createTaskQuery()
                .taskAssignee(userId)
                .orderByTaskCreateTime()
                .desc()
                .list();

        List<Map<String, Object>> taskList = tasks.stream().map(task -> {
            Map<String, Object> taskInfo = new HashMap<>();
            taskInfo.put("taskId", task.getId());
            taskInfo.put("taskName", task.getName());
            taskInfo.put("processInstanceId", task.getProcessInstanceId());
            taskInfo.put("createTime", task.getCreateTime());
            taskInfo.put("dueDate", task.getDueDate());

            // 获取流程变量
            Map<String, Object> variables = taskService.getVariables(task.getId());
            taskInfo.put("applicant", variables.get("applicant"));
            taskInfo.put("leaveType", variables.get("leaveType"));
            taskInfo.put("leaveDays", variables.get("leaveDays"));
            taskInfo.put("startDate", variables.get("startDate"));

            return taskInfo;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(taskList);
    }

    /**
     * 审批任务
     * @param taskId 任务id
     * @param approved 审批变量
     * @param comment 审批意见
     */
    @PostMapping("/approve/{taskId}")
    public ResponseEntity<Map<String, Object>> approveTask(
            @PathVariable String taskId,
            @RequestParam Boolean approved,
            @RequestParam(required = false) String comment) {

        Map<String, Object> variables = new HashMap<>();

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            throw new RuntimeException("任务不存在");
        }

        // 根据任务ID设置对应的审批变量
        if ("UserTask_ManagerApprove".equals(task.getTaskDefinitionKey())) {
            variables.put("managerApproved", approved);
            variables.put("managerComment", comment);
        } else if ("UserTask_DirectorApprove".equals(task.getTaskDefinitionKey())) {
            variables.put("directorApproved", approved);
            variables.put("directorComment", comment);
        }

        taskService.complete(taskId, variables);

        Map<String, Object> result = new HashMap<>();
        result.put("message", "审批完成");
        result.put("taskId", taskId);
        result.put("approved", approved);

        return ResponseEntity.ok(result);
    }

    /**
     * 获取流程历史
     * @param processInstanceId 请假实例id
     */
    @GetMapping("/history/{processInstanceId}")
    public ResponseEntity<Map<String, Object>> getProcessHistory(@PathVariable String processInstanceId) {
        HistoricProcessInstance processInstance = historyService
                .createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();

        List<HistoricActivityInstance> activities = historyService
                .createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                .orderByHistoricActivityInstanceStartTime()
                .asc()
                .list();

        Map<String, Object> history = new HashMap<>();
        history.put("processInstance", processInstance);
        history.put("activities", activities);

        return ResponseEntity.ok(history);
    }

    private String getDepartment(String userId) {
        // 模拟根据用户ID获取部门信息
        // 实际项目中应该调用用户服务
        return "manager_tech"; // 返回部门代码
    }
}

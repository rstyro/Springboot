## 引言

### 为什么需要工作流引擎？

在当今快速变化的商业环境中，企业需要处理越来越复杂的业务流程。想象一下：一个员工请假申请需要经过部门经理审批、HR备案、财务记录等多个环节；一个电商订单需要经历库存检查、支付确认、发货通知、物流跟踪等步骤。这些业务流程如果硬编码在系统中，不仅难以维护，更无法快速适应业务变化。



## 一、什么是Camunda？

- Camunda 是一个开源的工作流和业务流程管理平台，基于BPMN 2.0（业务流程模型与标记）标准构建。它提供了一个强大的流程引擎，允许开发人员将复杂的业务流程建模、执行、监控和优化。



### 1、传统开发 vs Camunda开发的对比：



```java
// 传统硬编码方式 - 紧密耦合，难以维护
public class LeaveApplicationService {
    public void applyLeave(LeaveRequest request) {
        // 1. 保存申请
        leaveRepository.save(request);
        
        // 2. 通知部门经理
        emailService.notifyManager(request);
        
        // 3. 如果经理批准，通知HR
        // 4. 如果HR通过，更新考勤系统
        // ... 更多嵌套的条件判断
    }
}

// 使用Camunda - 关注点分离，易于维护
@Service
public class LeaveApplicationService {
    
    @Autowired
    private RuntimeService runtimeService;
    
    public void applyLeave(LeaveRequest request) {
        // 启动流程，具体步骤在BPMN图中定义
        runtimeService.startProcessInstanceByKey(
            "LeaveProcess", 
            Variables.putValue("leaveRequest", request)
        );
    }
}
```



### 2、核心组件

| 组件                 | 功能描述                       | 适用场景         |
| :------------------- | :----------------------------- | :--------------- |
| **Camunda Engine**   | 核心流程引擎，负责执行BPMN流程 | 嵌入到Java应用中 |
| **Camunda Modeler**  | 图形化流程设计工具             | 业务流程建模     |
| **Camunda Tasklist** | 用户任务管理界面               | 人工任务处理     |
| **Camunda Cockpit**  | 流程监控和管理控制台           | 流程运维和监控   |
| **Camunda Optimize** | 流程分析和优化工具             | 性能分析和改进   |





## 二、Springboot快速开始

### 1、引入依赖


```text
<!-- springboot集成 -->
<dependency>
    <groupId>org.camunda.bpm.springboot</groupId>
    <artifactId>camunda-bpm-spring-boot-starter</artifactId>
    <version>${camunda.version}</version>
</dependency>
<!-- Camunda 提供的 Web 界面（如 Tasklist、Cockpit） -->
<dependency>
    <groupId>org.camunda.bpm.springboot</groupId>
    <artifactId>camunda-bpm-spring-boot-starter-webapp</artifactId>
    <version>${camunda.version}</version>
</dependency>

<!-- 扩展和可选 提供REST API，允许外部应用通过HTTP协议与引擎交互 -->
<dependency>
    <groupId>org.camunda.bpm.springboot</groupId>
    <artifactId>camunda-bpm-spring-boot-starter-rest</artifactId>
    <version>${camunda.version}</version>
</dependency>

<!-- Mysql驱动 -->
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <version>${mysql.version}</version>
</dependency>

```

### 2、配置yml

```yml
server:
  port: 8081

camunda.bpm:
  database:
    type: mysql
    schema-update: true  # 首次启动设置为true，自动创建表
  admin-user:
    id: admin  #用户名
    password: admin  #密码
    firstName: rstyro-
  filter:
    create: All tasks
  # 自动部署resources/下的BPMN文件
  auto-deployment-enabled: true
  # 历史级别: none, activity, audit, full
  history-level: full
  generic-properties:
    properties:
      historyTimeToLive: P30D  # 设置全局默认历史记录生存时间为30天
      enforceHistoryTimeToLive: false  # 可选：禁用强制TTL检查
  # 作业执行配置
  job-execution:
    enabled: true
    core-pool-size: 3
    max-pool-size: 10

# mysql连接信息
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    type: com.mysql.cj.jdbc.MysqlDataSource
    url: jdbc:mysql://localhost:3306/camunda
    username: root
    password: root
  jackson:
    date-format: yyyy-MM-dd HH:mm:ss
    time-zone: GMT+8

# 日志配置
logging:
  level:
    org.camunda: INFO
    org.springframework.web: INFO
```


### 3、camunda的表解释


| 表类别与前缀            | 核心职责                                       | 数据生命周期特点                                       | 代表性数据表                                          |
| :---------------------- | :--------------------------------------------- | :----------------------------------------------------- | :---------------------------------------------------- |
| **ACT_GE_*** (通用数据) | 存储引擎的二进制资源、属性配置和版本日志。     | 静态或长期存在，与流程定义同生命周期。                 | `ACT_GE_BYTEARRAY`, `ACT_GE_PROPERTY`                 |
| **ACT_RE_*** (资源存储) | 存储流程定义、决策规则等“静态”部署资源。       | 静态数据，部署后一般不变化，是流程的蓝图。             | `ACT_RE_PROCDEF`, `ACT_RE_DEPLOYMENT`                 |
| **ACT_RU_*** (运行时)   | 存储正在运行的流程实例、任务、变量等实时数据。 | **临时数据**，流程实例结束后立即被删除，保持表小而快。 | `ACT_RU_TASK`, `ACT_RU_EXECUTION`, `ACT_RU_VARIABLE`  |
| **ACT_HI_*** (历史记录) | 记录所有流程实例的完整历史、活动和变量变更。   | **历史数据**，长期保存，用于查询、报告与审计。         | `ACT_HI_PROCINST`, `ACT_HI_ACTINST`, `ACT_HI_VARINST` |
| **ACT_ID_*** (身份认证) | 管理用户、用户组以及他们之间的关联关系。       | 基础主数据，独立于流程生命周期。                       | `ACT_ID_USER`, `ACT_ID_GROUP`, `ACT_ID_MEMBERSHIP`    |

### 4、业务流程建模


#### 安装Camunda Modeler

我们一般会在`Camunda Modeler` 画出整个工作流的流程，然后导出 `.bpmn` 文件，然后在代码里面加载文件，进行编码的。

- Camunda Modeler下载地址：[https://camunda.com/download/modeler/](https://camunda.com/download/modeler/)
- 下载安装完成之后，我们可以新建一个请假流程。



![请假工作流流程](leave.png)




我们的BPMN文件内容放在`src/main/resources/process/leave.bpmn`中：

```text
<?xml version="1.0" encoding="UTF-8"?>
<bpmn:definitions
    xmlns:bpmn="http://www.omg.org/spec/BPMN/20100524/MODEL"
    xmlns:camunda="http://camunda.org/schema/1.0/bpmn"
    xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI"
    xmlns:dc="http://www.omg.org/spec/DD/20100524/DC"
    xmlns:di="http://www.omg.org/spec/DD/20100524/DI"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:modeler="http://camunda.org/schema/modeler/1.0" id="Definitions_LeaveProcess" targetNamespace="http://bpmn.io/schema/bpmn" exporter="Camunda Modeler" exporterVersion="5.23.0" modeler:executionPlatform="Camunda Platform" modeler:executionPlatformVersion="7.21.0">
    <bpmn:process id="LeaveProcess" name="请假审批流程" isExecutable="true" camunda:historyTimeToLive="P30D">
        <bpmn:startEvent id="StartEvent_1" name="请假开始">
            <bpmn:outgoing>Flow_StartToApply</bpmn:outgoing>
        </bpmn:startEvent>
        <bpmn:userTask id="UserTask_ApplyLeave" name="提交请假申请" camunda:assignee="${applicant}">
            <bpmn:extensionElements>
                <camunda:formData>
                    <camunda:formField id="leaveType" label="请假类型" type="string" defaultValue="年假">
                        <camunda:value id="annual">年假</camunda:value>
                        <camunda:value id="sick">病假</camunda:value>
                        <camunda:value id="personal">事假</camunda:value>
                    </camunda:formField>
                    <camunda:formField id="startDate" label="开始时间" type="string">
                        <camunda:properties>
                            <camunda:property id="datePattern" value="yyyy-MM-dd HH:mm:ss" />
                        </camunda:properties>
                    </camunda:formField>
                    <camunda:formField id="endDate" label="结束时间" type="string">
                        <camunda:properties>
                            <camunda:property id="datePattern" value="yyyy-MM-dd HH:mm:ss" />
                        </camunda:properties>
                    </camunda:formField>
                    <camunda:formField id="leaveDays" label="请假天数" type="long" />
                    <camunda:formField id="reason" label="请假事由" type="string" />
                </camunda:formData>
            </bpmn:extensionElements>
            <bpmn:incoming>Flow_StartToApply</bpmn:incoming>
            <bpmn:outgoing>Flow_ApplyToGateway</bpmn:outgoing>
        </bpmn:userTask>
        <bpmn:exclusiveGateway id="Gateway_ApprovalPath" name="审批路径判断">
            <bpmn:incoming>Flow_ApplyToGateway</bpmn:incoming>
            <bpmn:outgoing>Flow_GatewayToManager</bpmn:outgoing>
            <bpmn:outgoing>Flow_GatewayToDirector</bpmn:outgoing>
        </bpmn:exclusiveGateway>
        <bpmn:userTask id="UserTask_ManagerApprove" name="部门经理审批" camunda:assignee="${departmentManager}">
            <bpmn:extensionElements>
                <camunda:formData>
                    <camunda:formField id="managerApproved" label="审批结果" type="boolean">
                        <camunda:value id="true">同意</camunda:value>
                        <camunda:value id="false">拒绝</camunda:value>
                    </camunda:formField>
                    <camunda:formField id="managerComment" label="审批意见" type="string" />
                </camunda:formData>
            </bpmn:extensionElements>
            <bpmn:incoming>Flow_GatewayToManager</bpmn:incoming>
            <bpmn:outgoing>Flow_ManagerToEnd</bpmn:outgoing>
            <bpmn:outgoing>Flow_ManagerReject</bpmn:outgoing>
        </bpmn:userTask>
        <bpmn:userTask id="UserTask_DirectorApprove" name="总监审批" camunda:assignee="${director}">
            <bpmn:extensionElements>
                <camunda:formData>
                    <camunda:formField id="directorApproved" label="审批结果" type="boolean">
                        <camunda:value id="true">同意</camunda:value>
                        <camunda:value id="false">拒绝</camunda:value>
                    </camunda:formField>
                    <camunda:formField id="directorComment" label="审批意见" type="string" />
                </camunda:formData>
            </bpmn:extensionElements>
            <bpmn:incoming>Flow_GatewayToDirector</bpmn:incoming>
            <bpmn:outgoing>Flow_DirectorToEnd</bpmn:outgoing>
            <bpmn:outgoing>Flow_DirectorReject</bpmn:outgoing>
        </bpmn:userTask>
        <bpmn:serviceTask id="ServiceTask_HRRecord" name="人事备案" camunda:class="top.lrshuai.camunda.service.HRRecordService">
            <bpmn:incoming>Flow_ManagerToEnd</bpmn:incoming>
            <bpmn:incoming>Flow_DirectorToEnd</bpmn:incoming>
            <bpmn:outgoing>Flow_HRToEnd</bpmn:outgoing>
        </bpmn:serviceTask>
        <bpmn:serviceTask id="ServiceTask_NotifyApplicant" name="通知申请人" camunda:class="top.lrshuai.camunda.service.NotificationService">
            <bpmn:incoming>Flow_ManagerReject</bpmn:incoming>
            <bpmn:incoming>Flow_DirectorReject</bpmn:incoming>
            <bpmn:outgoing>Flow_NotifyToEnd</bpmn:outgoing>
        </bpmn:serviceTask>
        <bpmn:endEvent id="EndEvent_Approved" name="审批通过">
            <bpmn:incoming>Flow_HRToEnd</bpmn:incoming>
        </bpmn:endEvent>
        <bpmn:endEvent id="EndEvent_Rejected" name="审批拒绝">
            <bpmn:incoming>Flow_NotifyToEnd</bpmn:incoming>
        </bpmn:endEvent>
        <bpmn:sequenceFlow id="Flow_StartToApply" sourceRef="StartEvent_1" targetRef="UserTask_ApplyLeave" />
        <bpmn:sequenceFlow id="Flow_ApplyToGateway" sourceRef="UserTask_ApplyLeave" targetRef="Gateway_ApprovalPath" />
        <bpmn:sequenceFlow id="Flow_GatewayToManager" sourceRef="Gateway_ApprovalPath" targetRef="UserTask_ManagerApprove">
            <bpmn:conditionExpression xsi:type="bpmn:tFormalExpression">${leaveDays &lt;= 3}</bpmn:conditionExpression>
        </bpmn:sequenceFlow>
        <bpmn:sequenceFlow id="Flow_GatewayToDirector" sourceRef="Gateway_ApprovalPath" targetRef="UserTask_DirectorApprove">
            <bpmn:conditionExpression xsi:type="bpmn:tFormalExpression">${leaveDays &gt; 3}</bpmn:conditionExpression>
        </bpmn:sequenceFlow>
        <bpmn:sequenceFlow id="Flow_ManagerToEnd" sourceRef="UserTask_ManagerApprove" targetRef="ServiceTask_HRRecord">
            <bpmn:conditionExpression xsi:type="bpmn:tFormalExpression">${managerApproved == true}</bpmn:conditionExpression>
        </bpmn:sequenceFlow>
        <bpmn:sequenceFlow id="Flow_ManagerReject" sourceRef="UserTask_ManagerApprove" targetRef="ServiceTask_NotifyApplicant">
            <bpmn:conditionExpression xsi:type="bpmn:tFormalExpression">${managerApproved == false}</bpmn:conditionExpression>
        </bpmn:sequenceFlow>
        <bpmn:sequenceFlow id="Flow_DirectorToEnd" sourceRef="UserTask_DirectorApprove" targetRef="ServiceTask_HRRecord">
            <bpmn:conditionExpression xsi:type="bpmn:tFormalExpression">${directorApproved == true}</bpmn:conditionExpression>
        </bpmn:sequenceFlow>
        <bpmn:sequenceFlow id="Flow_DirectorReject" sourceRef="UserTask_DirectorApprove" targetRef="ServiceTask_NotifyApplicant">
            <bpmn:conditionExpression xsi:type="bpmn:tFormalExpression">${directorApproved == false}</bpmn:conditionExpression>
        </bpmn:sequenceFlow>
        <bpmn:sequenceFlow id="Flow_HRToEnd" sourceRef="ServiceTask_HRRecord" targetRef="EndEvent_Approved" />
        <bpmn:sequenceFlow id="Flow_NotifyToEnd" sourceRef="ServiceTask_NotifyApplicant" targetRef="EndEvent_Rejected" />
    </bpmn:process>
    <bpmndi:BPMNDiagram id="BPMNDiagram_1">
        <bpmndi:BPMNPlane id="BPMNPlane_1" bpmnElement="LeaveProcess">
            <bpmndi:BPMNShape id="_BPMNShape_StartEvent_2" bpmnElement="StartEvent_1">
                <dc:Bounds x="173" y="152" width="36" height="36" />
                <bpmndi:BPMNLabel>
                    <dc:Bounds x="169" y="188" width="44" height="14" />
                </bpmndi:BPMNLabel>
            </bpmndi:BPMNShape>
            <bpmndi:BPMNShape id="UserTask_ApplyLeave_di" bpmnElement="UserTask_ApplyLeave">
                <dc:Bounds x="260" y="130" width="100" height="80" />
            </bpmndi:BPMNShape>
            <bpmndi:BPMNShape id="Gateway_ApprovalPath_di" bpmnElement="Gateway_ApprovalPath" isMarkerVisible="true">
                <dc:Bounds x="410" y="145" width="50" height="50" />
                <bpmndi:BPMNLabel>
                    <dc:Bounds x="402" y="195" width="67" height="14" />
                </bpmndi:BPMNLabel>
            </bpmndi:BPMNShape>
            <bpmndi:BPMNShape id="UserTask_ManagerApprove_di" bpmnElement="UserTask_ManagerApprove">
                <dc:Bounds x="510" y="80" width="100" height="80" />
            </bpmndi:BPMNShape>
            <bpmndi:BPMNShape id="UserTask_DirectorApprove_di" bpmnElement="UserTask_DirectorApprove">
                <dc:Bounds x="510" y="180" width="100" height="80" />
            </bpmndi:BPMNShape>
            <bpmndi:BPMNShape id="EndEvent_Approved_di" bpmnElement="EndEvent_Approved">
                <dc:Bounds x="1042" y="82" width="36" height="36" />
                <bpmndi:BPMNLabel>
                    <dc:Bounds x="1038" y="118" width="45" height="14" />
                </bpmndi:BPMNLabel>
            </bpmndi:BPMNShape>
            <bpmndi:BPMNShape id="EndEvent_Rejected_di" bpmnElement="EndEvent_Rejected">
                <dc:Bounds x="1052" y="322" width="36" height="36" />
                <bpmndi:BPMNLabel>
                    <dc:Bounds x="1047" y="298" width="45" height="14" />
                </bpmndi:BPMNLabel>
            </bpmndi:BPMNShape>
            <bpmndi:BPMNShape id="ServiceTask_NotifyApplicant_di" bpmnElement="ServiceTask_NotifyApplicant">
                <dc:Bounds x="690" y="310" width="100" height="80" />
            </bpmndi:BPMNShape>
            <bpmndi:BPMNShape id="ServiceTask_HRRecord_di" bpmnElement="ServiceTask_HRRecord">
                <dc:Bounds x="750" y="80" width="100" height="80" />
            </bpmndi:BPMNShape>
            <bpmndi:BPMNEdge id="Flow_StartToApply_di" bpmnElement="Flow_StartToApply">
                <di:waypoint x="209" y="170" />
                <di:waypoint x="260" y="170" />
            </bpmndi:BPMNEdge>
            <bpmndi:BPMNEdge id="Flow_ApplyToGateway_di" bpmnElement="Flow_ApplyToGateway">
                <di:waypoint x="360" y="170" />
                <di:waypoint x="410" y="170" />
            </bpmndi:BPMNEdge>
            <bpmndi:BPMNEdge id="Flow_GatewayToManager_di" bpmnElement="Flow_GatewayToManager">
                <di:waypoint x="435" y="145" />
                <di:waypoint x="435" y="120" />
                <di:waypoint x="510" y="120" />
            </bpmndi:BPMNEdge>
            <bpmndi:BPMNEdge id="Flow_GatewayToDirector_di" bpmnElement="Flow_GatewayToDirector">
                <di:waypoint x="435" y="195" />
                <di:waypoint x="435" y="220" />
                <di:waypoint x="510" y="220" />
            </bpmndi:BPMNEdge>
            <bpmndi:BPMNEdge id="Flow_ManagerToEnd_di" bpmnElement="Flow_ManagerToEnd">
                <di:waypoint x="610" y="120" />
                <di:waypoint x="750" y="120" />
            </bpmndi:BPMNEdge>
            <bpmndi:BPMNEdge id="Flow_ManagerReject_di" bpmnElement="Flow_ManagerReject">
                <di:waypoint x="560" y="160" />
                <di:waypoint x="560" y="350" />
                <di:waypoint x="690" y="350" />
            </bpmndi:BPMNEdge>
            <bpmndi:BPMNEdge id="Flow_DirectorToEnd_di" bpmnElement="Flow_DirectorToEnd">
                <di:waypoint x="610" y="220" />
                <di:waypoint x="710" y="220" />
                <di:waypoint x="710" y="120" />
                <di:waypoint x="750" y="120" />
            </bpmndi:BPMNEdge>
            <bpmndi:BPMNEdge id="Flow_DirectorReject_di" bpmnElement="Flow_DirectorReject">
                <di:waypoint x="610" y="240" />
                <di:waypoint x="650" y="240" />
                <di:waypoint x="650" y="320" />
                <di:waypoint x="690" y="320" />
            </bpmndi:BPMNEdge>
            <bpmndi:BPMNEdge id="Flow_HRToEnd_di" bpmnElement="Flow_HRToEnd">
                <di:waypoint x="850" y="120" />
                <di:waypoint x="941" y="120" />
                <di:waypoint x="941" y="100" />
                <di:waypoint x="1042" y="100" />
            </bpmndi:BPMNEdge>
            <bpmndi:BPMNEdge id="Flow_NotifyToEnd_di" bpmnElement="Flow_NotifyToEnd">
                <di:waypoint x="790" y="350" />
                <di:waypoint x="921" y="350" />
                <di:waypoint x="921" y="340" />
                <di:waypoint x="1052" y="340" />
            </bpmndi:BPMNEdge>
        </bpmndi:BPMNPlane>
    </bpmndi:BPMNDiagram>
</bpmn:definitions>

```



### 5、Java服务实现

创建相应的Java服务类来处理业务流程：



```java
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
            variables.put("departmentManager", "manager_" + getDepartment(application.getApplicant()));
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
        return "tech"; // 返回部门代码
    }
}


// 人事备案
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


// 发送通知
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
```



- 上面提供：启动请假流程、用户待办任务列表、还有审批接口、获取历史接口
- 从发起请求流程开始一步一步完成整个流程的



![启动请假](api1.png)


![获取用户待办任务](api2.png)

因为我这里设置开始流程之后，必须经过一个由申请人自己处理的“提交请假申请”任务，所以申请人能看到自己的任务（也就是刚提交的请假流程）



![工作流进度示例图](processes1.png)


![审批流程](api3.png)

当申请人审批之后，就正常的流转到排他网关，通过申请的请假天数判断是给经理审批（<=3天）还是总监审批（>3天）。



我们这里设置的是3天，所以还是经理审批，经理审批的分配变量=`${departmentManager}`,总监分配变量=`${director}`。在代码我们可以看到`departmentManager`=`manager_tech`。



![经理工作流进度示例图](processes2.png)






![用户确认请假事件](api4.png)



所以通过`manager_tech`这个人得到待审核的任务进行审核,然后又调用审批接口，完成整个流程。



![用户确认请假事件](api5.png)


![通知用户结果示例图](processes3.png)


## 三、总结



Camunda工作流引擎就像业务流程的"操作系统"，它让复杂的业务流程变得**可视化、可管理、可监控**。



#### 什么时候应该使用工作流引擎？

- 业务流程复杂，涉及多个环节和角色
- 业务流程频繁变更
- 需要详细的过程跟踪和审计
- 有跨系统流程整合需求



#### 资源获取：

本文完整代码已上传至 GitHub，欢迎 Star ⭐ 和 Fork： [https://github.com/rstyro/Springboot/tree/master/springboot-camunda](https://github.com/rstyro/Springboot/tree/master/springboot-camunda)



**欢迎分享你的经验**：
在实际使用工作流时，你有哪些独到的见解或踩坑经验？欢迎在评论区交流讨论，让我们一起进步

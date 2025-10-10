## 一、什么是Camunda？

- Camunda 是一个开源的工作流和业务流程管理平台，基于BPMN 2.0（业务流程模型与标记）标准构建。它提供了一个强大的流程引擎，允许开发人员将复杂的业务流程建模、执行、监控和优化。



### 1、核心组件

| 组件                 | 功能描述                       | 适用场景         |
| :------------------- | :----------------------------- | :--------------- |
| **Camunda Engine**   | 核心流程引擎，负责执行BPMN流程 | 嵌入到Java应用中 |
| **Camunda Modeler**  | 图形化流程设计工具             | 业务流程建模     |
| **Camunda Tasklist** | 用户任务管理界面               | 人工任务处理     |
| **Camunda Cockpit**  | 流程监控和管理控制台           | 流程运维和监控   |
| **Camunda Optimize** | 流程分析和优化工具             | 性能分析和改进   |


## 二、Springboot快速开始

### 1、引入依赖

```xml
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


### 4、
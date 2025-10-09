package top.lrshuai.camunda.config;

import jakarta.annotation.Resource;
import org.camunda.bpm.engine.RepositoryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

//@Component
public class ProcessAutoDeployerConfig implements CommandLineRunner {

    @Resource
    private RepositoryService repositoryService;

    @Override
    public void run(String... args) throws Exception {
        // 自动部署resources目录下的BPMN文件
        repositoryService.createDeployment()
                .name("LeaveProcessDeployment")
                .addClasspathResource("process/leave.bpmn") // 替换为您的BPMN文件路径
                .deploy();

        System.out.println("流程部署完成");
    }
}

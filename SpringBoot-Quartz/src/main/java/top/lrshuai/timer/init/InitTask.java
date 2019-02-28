package top.lrshuai.timer.init;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import top.lrshuai.timer.task.entity.JobStatus;
import top.lrshuai.timer.task.entity.Quartz;
import top.lrshuai.timer.task.service.IQuartzService;

import java.time.LocalDateTime;


@Component
public class InitTask implements ApplicationRunner {
	private final static Logger LOGGER = LoggerFactory.getLogger(InitTask.class);

	@Autowired
    private Scheduler scheduler;

	@Autowired
	private IQuartzService quartzService;

	@Override
    public void run(ApplicationArguments var) throws Exception{
    	Long count = quartzService.getQuartCount();
    	if(count==0){
    		LOGGER.info("初始化测试任务");
    		Quartz quartz = new Quartz();
            quartz.setJobGroup("test");
            quartz.setJobName("测试");
    		quartz.setDescription("测试任务,每30秒后台打印数据");
    		quartz.setJobClassName("top.lrshuai.timer.job.HelloJob");
    		quartz.setCronExpression("0/30 * * * * ?");

   	        Class cls = Class.forName(quartz.getJobClassName()) ;
   	        cls.newInstance();
   	        //构建job信息
   	        JobDetail job = JobBuilder.newJob(cls).withIdentity(quartz.getJobName(), quartz.getJobGroup()).withDescription(quartz.getDescription()).build();
   	        //添加JobDataMap数据
   	        job.getJobDataMap().put("blog", "https://rstyro.github.io/blog/");
   	        // 触发时间点
   	        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(quartz.getCronExpression());
   	        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger"+quartz.getJobName(), quartz.getJobGroup())
   	                .startNow().withSchedule(cronScheduleBuilder).build();
   	        //交由Scheduler安排触发
   	        scheduler.scheduleJob(job, trigger);
            TriggerKey triggerKey = TriggerKey.triggerKey(quartz.getJobName(), quartz.getJobGroup());
            Trigger.TriggerState triggerState = scheduler.getTriggerState(triggerKey);
            quartz.setJobStatus(JobStatus.RUN.getStatus());
            quartz.setCreateTime(LocalDateTime.now());
			quartzService.save(quartz);
    	}
    }

}
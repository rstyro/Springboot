package top.lrshuai.timer.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Executor;

//@Component
public class TaskConfig {
//    //配置数据源
//    //这里可以不使用@Bean交给spring管理,否则可能出现默认数据源的问题.
//    //@Bean
//    public DataSource druidDataSource() {
//        DruidDataSource dataSource = new DruidDataSource();
//        dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/quartz?serverTimezone=GMT%2B8");
//        dataSource.setUsername("root");
//        dataSource.setPassword("root");
//        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
//        return dataSource;
//    }
//
//    //配置quartz配置文件
//    @Bean
//    public Properties quartzProperties() throws IOException {
//        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
//        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
//        // 在quartz.properties中的属性被读取并注入后再初始化对象
//        propertiesFactoryBean.afterPropertiesSet();
//        return propertiesFactoryBean.getObject();
//    }
//
//
//
//
//    //配置任务调度工厂,用来生成任务调度器,这是quartz的核心
//    @Bean
//    public SchedulerFactoryBean schedulerFactoryBean() throws IOException {
//        SchedulerFactoryBean factory = new SchedulerFactoryBean();
//        //开启更新job
//        factory.setOverwriteExistingJobs(true);
//        //如果不配置就会使用quartz.properties中的instanceName
//        //factory.setSchedulerName("Cluster_Scheduler");
//        //配置数据源,这是quartz使用的表的数据库存放位置
//        factory.setDataSource(druidDataSource());
//        //设置实例在spring容器中的key
//        factory.setApplicationContextSchedulerContextKey("applicationContext");
//        //配置线程池
//        factory.setTaskExecutor(schedulerThreadPool());
//        //配置配置文件
//        factory.setQuartzProperties(quartzProperties());
//        //配置任务执行规则,参数是一个可变数组
//        return factory;
//    }
//    //开启当前的任务调度器
//    @Bean
//    public Scheduler scheduler() throws Exception {
//        Scheduler scheduler = schedulerFactoryBean().getScheduler();
//        TriggerKey triggerKey1 = TriggerKey.triggerKey("trigger1", "TriggerTest11111");
//        /*========如果有必要可以配置删除任务,开始====================*/
//        //停止触发器
//        scheduler.pauseTrigger(triggerKey1);
//        //移除触发器
//        scheduler.unscheduleJob(triggerKey1);
//        JobKey jobKey1 = JobKey.jobKey("job1111------", "quartzTest--------");
//        //删除任务
//        boolean b = scheduler.deleteJob(jobKey1);
//        System.out.println(b);
//        /*=========结束====================*/
//        scheduler.start();
//        return scheduler;
//    }
//
//    //线程池配置
//    @Bean
//    public Executor schedulerThreadPool() {
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setCorePoolSize(10);
//        executor.setMaxPoolSize(20);
//        executor.setQueueCapacity(50);
//        return executor;
//    }
}

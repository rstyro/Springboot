package top.rstyro.shiro.shiro.executor;

import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

//@Configuration
public class ExecutorConfig {

    /**
     * 线程池
     */
    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        SubjectAwareTaskExecutor executor = new SubjectAwareTaskExecutor();
        //线程池维护线程的最少数量
        executor.setCorePoolSize(50);
        //允许的空闲时间
        executor.setKeepAliveSeconds(200);
        //线程池维护线程的最大数量
        executor.setMaxPoolSize(100);
        //缓存队列
        executor.setQueueCapacity(40);
        //对拒绝task的处理策略
        ThreadPoolExecutor.CallerRunsPolicy callerRunsPolicy = new ThreadPoolExecutor.CallerRunsPolicy();
        executor.setRejectedExecutionHandler(callerRunsPolicy);
        executor.setThreadNamePrefix("Custom Thread-");
        executor.initialize();
        return executor;
    }
}

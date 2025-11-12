package top.lrshuai.shedlock.config;

import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.redis.spring.RedisLockProvider;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;

/**
 * shedlock 配置
 */
@Configuration
// 默认锁最大持有时间，30分钟
@EnableSchedulerLock(defaultLockAtMostFor = "30m")
public class ShedlockConfig {

    /**
     * 锁的提供者实现，使用redis
     */
    @Bean
    public LockProvider lockProvider(RedisConnectionFactory connectionFactory) {
        return new RedisLockProvider.Builder(connectionFactory)
                .keyPrefix("task-lock")
                .build();
    }
}

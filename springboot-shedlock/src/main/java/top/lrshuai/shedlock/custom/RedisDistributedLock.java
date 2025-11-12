package top.lrshuai.shedlock.custom;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.time.Duration;
import java.util.Objects;
import java.util.UUID;

@Component
public class RedisDistributedLock {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private static final String LOCK_PREFIX = "scheduled:lock:";

    /**
     * 尝试获取分布式锁
     *
     * @param key      锁的key
     * @param value    锁的value（通常为机器标识，如IP+PID）
     * @param holdTime 锁持有时间（秒）
     * @return 是否获取成功
     */
    public boolean tryLock(String key, String value, long holdTime) {
        String fullKey = LOCK_PREFIX + key;
        // 使用SET NX EX命令，保证原子性：如果key不存在则设置，并设置过期时间。
        Boolean success = stringRedisTemplate.opsForValue().setIfAbsent(fullKey, value, Duration.ofSeconds(holdTime));
        return Boolean.TRUE.equals(success);
    }

    /**
     * 释放分布式锁
     * 注意：释放时需验证value，确保是自己加的锁，防止误删。
     */
    public void unlock(String key, String value) {
        String fullKey = LOCK_PREFIX + key;
        String currentValue = stringRedisTemplate.opsForValue().get(fullKey);
        // 比较当前锁的值是否与自己设置的值一致，一致才删除
        if (Objects.equals(value, currentValue)) {
            stringRedisTemplate.delete(fullKey);
        }
        // 如果不一致，说明锁已过期或被其他线程/进程获取，无需操作。
    }

    /**
     * 生成锁的value，用于标识当前持有锁的实例。
     * 格式：IP:PID:ThreadID
     */
    public String generateLockValue() {
        try {
            String ip = InetAddress.getLocalHost().getHostAddress();
            long pid = ProcessHandle.current().pid();
            long threadId = Thread.currentThread().getId();
            return String.format("%s:%d:%d", ip, pid, threadId);
        } catch (Exception e) {
            return UUID.randomUUID().toString();
        }
    }
}

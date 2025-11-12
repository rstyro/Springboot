package top.lrshuai.shedlock.custom;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ScheduledLockAspect {

    @Resource
    private RedisDistributedLock redisDistributedLock;

    @Around("@annotation(scheduledLock)")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint, ScheduledLock scheduledLock) throws Throwable {
        String lockKey = scheduledLock.lockKey();
        long holdTime = scheduledLock.holdTime();
        String lockValue = redisDistributedLock.generateLockValue();

        boolean isLockAcquired = false;
        try {
            // 1. 尝试获取锁
            isLockAcquired = redisDistributedLock.tryLock(lockKey, lockValue, holdTime);
            if (!isLockAcquired) {
                log.warn("【定时任务锁】获取锁失败，任务即将跳过。lockKey: {}", lockKey);
                // 根据LockFailAction执行不同策略，这里简单返回null
                return null;
            }
            log.info("【定时任务锁】成功获取锁，开始执行任务。lockKey: {}, lockValue: {}", lockKey, lockValue);
            // 2. 获取锁成功，执行原定时任务方法
            return joinPoint.proceed();

        } finally {
            // 3. 无论如何，最终都尝试释放锁
            if (isLockAcquired) {
                try {
                    redisDistributedLock.unlock(lockKey, lockValue);
                    log.info("【定时任务锁】任务执行完毕，锁已释放。lockKey: {}", lockKey);
                } catch (Exception e) {
                    log.error("【定时任务锁】释放锁时发生异常。lockKey: " + lockKey, e);
                }
            }
        }
    }

}

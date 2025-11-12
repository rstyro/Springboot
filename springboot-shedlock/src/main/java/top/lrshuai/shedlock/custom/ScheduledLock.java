package top.lrshuai.shedlock.custom;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ScheduledLock {
    /**
     * 锁的key，用于唯一标识一个任务。
     */
    String lockKey();

    /**
     * 锁的持有时间（秒），超过这个时间锁会自动释放，防止死锁。
     * 默认30秒，应根据任务实际执行时间设置。
     */
    long holdTime() default 30L;
}

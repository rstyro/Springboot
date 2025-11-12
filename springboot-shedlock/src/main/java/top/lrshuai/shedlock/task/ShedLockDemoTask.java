package top.lrshuai.shedlock.task;

import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ShedLockDemoTask {

    @Scheduled(fixedDelay = 5000, initialDelay = 1000)
    @SchedulerLock(name = "checkOrderTimeout",lockAtMostFor = "1m",lockAtLeastFor = "10s")
    public void shedLockCheckOrderTimeout() throws InterruptedException {
        log.info("【ShedLockDemoTask】开始执行...,threadId:{},threadName={}", Thread.currentThread().getId(),Thread.currentThread().getName());
        log.info("【ShedLockDemoTask】执行中...,threadId:{},threadName={}", Thread.currentThread().getId(),Thread.currentThread().getName());
        Thread.sleep(30000);
        log.info("【ShedLockDemoTask】执行完毕。,threadId:{},threadName={}", Thread.currentThread().getId(),Thread.currentThread().getName());
    }
}

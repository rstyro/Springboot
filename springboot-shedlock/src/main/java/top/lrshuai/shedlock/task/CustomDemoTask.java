package top.lrshuai.shedlock.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.lrshuai.shedlock.custom.ScheduledLock;

@Slf4j
@Component
public class CustomDemoTask {

    @Scheduled(fixedDelay = 30000, initialDelay = 1000)
    @ScheduledLock(lockKey = "task:checkOrderTimeout")
    public void checkOrderTimeout() throws InterruptedException {
        log.info("【超时订单检测任务】开始执行...,threadId:{},threadName={}", Thread.currentThread().getId(),Thread.currentThread().getName());
        log.info("【超时订单检测任务】检测中...,threadId:{},threadName={}", Thread.currentThread().getId(),Thread.currentThread().getName());
        Thread.sleep(30000);
        log.info("【超时订单检测任务】执行完毕。,threadId:{},threadName={}", Thread.currentThread().getId(),Thread.currentThread().getName());
    }
}

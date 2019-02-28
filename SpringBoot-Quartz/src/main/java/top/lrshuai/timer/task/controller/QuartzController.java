package top.lrshuai.timer.task.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import top.lrshuai.timer.common.constant.ApiResultEnum;
import top.lrshuai.timer.common.constant.Result;
import top.lrshuai.timer.task.entity.JobStatus;
import top.lrshuai.timer.task.entity.Quartz;
import top.lrshuai.timer.task.entity.QuartzDTO;
import top.lrshuai.timer.task.service.IQuartzService;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author rstyro
 * @since 2019-02-26
 */
@RestController
@RequestMapping("/task/quartz")
public class QuartzController {

    private  Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private IQuartzService quartzService;

    @PostMapping("/add")
    public Result save(Quartz quartz){
        LOGGER.info("新增任务");
        try {
            List<Quartz> list = quartzService.list(new QueryWrapper<Quartz>().lambda().eq(Quartz::getJobGroup, quartz.getJobGroup()).eq(Quartz::getJobName, quartz.getJobName()));
            if(list != null && list.size() > 0){
                return Result.error(ApiResultEnum.TRIGGER_GROUP_AND_NAME_SAME);
            }
            System.out.println("Quartz="+ JSON.toJSONString(quartz));
            quartz.setCreateTime(LocalDateTime.now());
            quartz.setJobStatus(JobStatus.STOP.getStatus());
            quartzService.save(quartz);


        } catch (Exception e) {
            e.printStackTrace();
            return Result.error();
        }
        return Result.ok();
    }

    @GetMapping("/list")
    public Result list(QuartzDTO dto){
        LOGGER.info("任务列表");
        return Result.ok(quartzService.getQuartzPage(dto));
    }

    @PostMapping("/start")
    public Result start(Long id) throws Exception {
        LOGGER.info("任务列表");
        Quartz quartz = quartzService.getById(id);
        if(quartz.getJobStatus().equals(JobStatus.RUN.getStatus())){
            return Result.error(ApiResultEnum.TASK_IS_RUNING);
        }else if(quartz.getJobStatus().equals(JobStatus.PAUSED.getStatus())){
            return Result.error(ApiResultEnum.TASK_IS_PAUSE);
        }
        addJob(quartz);
        quartz.setJobStatus(JobStatus.RUN.getStatus());
        quartzService.updateById(quartz);
        return Result.ok();
    }

    /**
     * 启动任务
     * @param quartz
     * @throws Exception
     */
    public void addJob(Quartz quartz) throws Exception {
        Class cls = Class.forName(quartz.getJobClassName()) ;
        cls.newInstance();
        //构建job信息
        JobDetail job = JobBuilder.newJob(cls).withIdentity(quartz.getJobName(),quartz.getJobGroup()).withDescription(quartz.getDescription()).build();
        // 触发时间点
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(quartz.getCronExpression());
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger_"+quartz.getJobName(), quartz.getJobGroup()).withDescription(quartz.getDescription()).startNow().withSchedule(cronScheduleBuilder).build();
        //交由Scheduler安排触发
        scheduler.scheduleJob(job, trigger);
    }

    /**
     * 编辑
     * @param quartz
     * @return
     * @throws SchedulerException
     */
    @PostMapping("/edit")
    public Result edit(Quartz quartz) throws SchedulerException {
        LOGGER.info("任务列表");
        //如果是修改  展示旧的 任务
        if(quartz.getOldJobGroup()!=null){
            TriggerKey triggerKey = TriggerKey.triggerKey(quartz.getOldJobName(), quartz.getOldJobGroup());
            // 停止触发器
            scheduler.pauseTrigger(triggerKey);
            // 移除触发器
            scheduler.unscheduleJob(triggerKey);
            JobKey key = new JobKey(quartz.getOldJobName(),quartz.getOldJobGroup());
            scheduler.deleteJob(key);
            System.out.println("移除任务:"+JSON.toJSONString(key));
        }
        quartz.setJobStatus(JobStatus.STOP.getStatus());
        quartz.setModifyTime(LocalDateTime.now());
        quartzService.updateById(quartz);
        return Result.ok();
    }

    @GetMapping(value="/query")
    @ResponseBody
    public Result query(Long id) throws Exception {
        return quartzService.getDetail(id);
    }

    @PostMapping("/trigger")
    public  Result trigger(Quartz quartz, HttpServletResponse response) {
        LOGGER.info("触发任务");
        try {
            JobKey key = new JobKey(quartz.getJobName(),quartz.getJobGroup());
            scheduler.triggerJob(key);
        } catch (SchedulerException e) {
            e.printStackTrace();
            return Result.error();
        }
        return Result.ok();
    }

    @PostMapping("/pause")
    public  Result pause(Long id) {
        LOGGER.info("停止任务");
        try {
            Quartz quartz = quartzService.getById(id);
            if(quartz == null){
                return Result.error("操作异常");
            }
            if(JobStatus.PAUSED.getStatus().equals(quartz.getJobStatus())){
                //停止则恢复
                JobKey key = new JobKey(quartz.getJobName(),quartz.getJobGroup());
                scheduler.resumeJob(key);
                quartz.setJobStatus(JobStatus.RUN.getStatus());
            }else if(JobStatus.RUN.getStatus().equals(quartz.getJobStatus())){
                JobKey key = new JobKey(quartz.getJobName(),quartz.getJobGroup());
                scheduler.pauseJob(key);
                quartz.setJobStatus(JobStatus.PAUSED.getStatus());
            }else{
                return Result.error(ApiResultEnum.TASK_NOT_RUNING);
            }
            quartzService.updateById(quartz);
        } catch (SchedulerException e) {
            e.printStackTrace();
            return Result.error();
        }
        return Result.ok();
    }


    @PostMapping("/remove")
    public Result remove(Long id) {
        LOGGER.info("移除任务");
        try {
            Quartz quartz = quartzService.getById(id);
            if(quartz == null){
                return Result.error("操作异常");
            }
            TriggerKey triggerKey = TriggerKey.triggerKey(quartz.getJobName(), quartz.getJobGroup());
            // 停止触发器
            scheduler.pauseTrigger(triggerKey);
            // 移除触发器
            scheduler.unscheduleJob(triggerKey);
            // 删除任务
            scheduler.deleteJob(JobKey.jobKey(quartz.getJobName(), quartz.getJobGroup()));
            System.out.println("removeJob:"+JobKey.jobKey(quartz.getJobName()));
            quartzService.removeById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error();
        }
        return Result.ok();
    }
}

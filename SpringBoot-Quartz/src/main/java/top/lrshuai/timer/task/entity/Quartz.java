package top.lrshuai.timer.task.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author rstyro
 * @since 2019-02-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("task_quartz")
public class Quartz implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 任务分组
     */
    private String jobGroup;

    /**
     * 任务名
     */
    private String jobName;

    /**
     * 任务描述
     */
    private String description;

    /**
     * cron表达式
     */
    private String cronExpression;

    /**
     * 任务执行时调用哪个类的方法 包名+类名
     */
    private String jobClassName;

    /**
     * 任务状态
     */
    private String jobStatus;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新者
     */
    private String modifyBy;

    /**
     * 更新时间
     */
    private LocalDateTime modifyTime;

    @TableField(exist = false)
    private String oldJobName;//任务名称 用于修改
    @TableField(exist = false)
    private String oldJobGroup;//任务分组 用于修改


}

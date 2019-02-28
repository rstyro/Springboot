package top.lrshuai.timer.task.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import top.lrshuai.timer.common.constant.Result;
import top.lrshuai.timer.task.entity.Quartz;
import com.baomidou.mybatisplus.extension.service.IService;
import top.lrshuai.timer.task.entity.QuartzDTO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author rstyro
 * @since 2019-02-26
 */
public interface IQuartzService extends IService<Quartz> {
    public long getQuartCount() throws  Exception;
    public IPage<Quartz> getQuartzPage(QuartzDTO dto);
    public Result getDetail(Long id) throws  Exception;
}

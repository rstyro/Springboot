package top.lrshuai.timer.task.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import top.lrshuai.timer.task.entity.Quartz;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author rstyro
 * @since 2019-02-26
 */
@Component
public interface QuartzMapper extends BaseMapper<Quartz> {
    public long getQuartCount();
    IPage<Quartz> getQuartzListPage(Page page, @Param("keyword") String keyword);
}

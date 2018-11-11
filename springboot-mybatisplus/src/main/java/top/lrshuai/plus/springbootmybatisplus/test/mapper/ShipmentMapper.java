package top.lrshuai.plus.springbootmybatisplus.test.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import top.lrshuai.plus.springbootmybatisplus.test.entity.Shipment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2018-11-07
 */
@Component
public interface ShipmentMapper extends BaseMapper<Shipment> {

    IPage<Shipment> getListByPage(Page page, @Param("status") Integer status);
}

package top.lrshuai.plus.springbootmybatisplus.test.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import top.lrshuai.plus.springbootmybatisplus.test.entity.Shipment;
import com.baomidou.mybatisplus.extension.service.IService;
import top.lrshuai.plus.springbootmybatisplus.test.entity.dto.ListDTO;
import top.lrshuai.plus.springbootmybatisplus.test.entity.dto.TestDTO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jobob
 * @since 2018-11-07
 */
public interface IShipmentService extends IService<Shipment> {

    public Object getList(TestDTO dto);
    public IPage<Shipment> getListByPage(ListDTO dto);
}

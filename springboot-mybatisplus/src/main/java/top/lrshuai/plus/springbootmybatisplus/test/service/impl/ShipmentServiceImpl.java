package top.lrshuai.plus.springbootmybatisplus.test.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import top.lrshuai.plus.springbootmybatisplus.test.entity.Shipment;
import top.lrshuai.plus.springbootmybatisplus.test.entity.dto.ListDTO;
import top.lrshuai.plus.springbootmybatisplus.test.entity.dto.TestDTO;
import top.lrshuai.plus.springbootmybatisplus.test.mapper.ShipmentMapper;
import top.lrshuai.plus.springbootmybatisplus.test.service.IShipmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jobob
 * @since 2018-11-07
 */
@Service
public class ShipmentServiceImpl extends ServiceImpl<ShipmentMapper, Shipment> implements IShipmentService {

    @Autowired
    private ShipmentMapper shipmentMapper;

    @Override
    public Object getList(TestDTO dto) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if(StringUtils.isNotEmpty(dto.getOrderName())){
            queryWrapper.eq("order_number",dto.getOrderName());
        }
        return shipmentMapper.selectList(queryWrapper);
    }

    @Override
    public IPage<Shipment> getListByPage(ListDTO dto) {
        // 不进行 count sql 优化，解决 MP 无法自动优化 SQL 问题，这时候你需要自己查询 count 部分
        // page.setOptimizeCountSql(false);
        // 当 total 为非 0 时(默认为 0),分页插件不会进行 count 查询
        // 要点!! 分页返回的对象与传入的对象是同一个
        Page<Shipment> page = new Page<>();
        page.setSize(dto.getPageSize());
        page.setPages(dto.getPageNo());
        return shipmentMapper.getListByPage(page,dto.getStatus());
    }

}

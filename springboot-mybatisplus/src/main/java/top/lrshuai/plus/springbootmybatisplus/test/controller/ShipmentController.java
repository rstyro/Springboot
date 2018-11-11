package top.lrshuai.plus.springbootmybatisplus.test.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.lrshuai.plus.springbootmybatisplus.test.entity.dto.ListDTO;
import top.lrshuai.plus.springbootmybatisplus.test.entity.dto.TestDTO;
import top.lrshuai.plus.springbootmybatisplus.test.service.IShipmentService;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jobob
 * @since 2018-11-07
 */
@RestController
@RequestMapping("/shipment")
public class ShipmentController  {
    @Autowired
    private IShipmentService iShipmentService;

    @GetMapping("/test")
    public Object test(TestDTO dto){
        return  iShipmentService.getList(dto);
    }

    @GetMapping("/page")
    public Object pageTest(ListDTO dto){
        return  iShipmentService.getListByPage(dto);
    }
}

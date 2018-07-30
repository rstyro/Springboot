package top.lrshuai.i18n.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import top.lrshuai.i18n.constant.ResponseCodeEnum;
import top.lrshuai.i18n.dto.ResponseDTO;

/**
 * 接口测试类
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/api")
public class ApiController {
	
	@GetMapping("/test")
	public Object test(){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("key", "这里就是数据了");
		return new ResponseDTO(ResponseCodeEnum.SUCCESS,map);
	}

}

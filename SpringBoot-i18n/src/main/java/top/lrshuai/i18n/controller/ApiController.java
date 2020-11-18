package top.lrshuai.i18n.controller;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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
	@Autowired
	private MessageSource messageSource;
	
	@GetMapping("/test")
	public Object test(){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("key", "这里就是数据了");
		return new ResponseDTO(ResponseCodeEnum.SUCCESS,map);
	}

	@GetMapping("/test2")
	public Object test2(){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("key", "这里就是数据了");
		// 国际化获取
		map.put("us", messageSource.getMessage("200",null, Locale.US));
		map.put("JS", messageSource.getMessage("200",null, Locale.JAPAN));
		map.put("UK", messageSource.getMessage("200",null, Locale.KOREA));

		// 参数就是占位符
		map.put("us1", messageSource.getMessage("600",new String[]{"300","100"}, Locale.US));
		map.put("JS1", messageSource.getMessage("600",new String[]{"300","100"}, Locale.JAPAN));
		map.put("UK1", messageSource.getMessage("600",new String[]{"300","100"}, Locale.KOREA));
		return new ResponseDTO(ResponseCodeEnum.SUCCESS,map);
	}

}

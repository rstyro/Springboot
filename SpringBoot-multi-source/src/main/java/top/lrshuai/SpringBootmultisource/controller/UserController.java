package top.lrshuai.SpringBootmultisource.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import top.lrshuai.SpringBootmultisource.dto.UserDto;
import top.lrshuai.SpringBootmultisource.entity.RestBody;
import top.lrshuai.SpringBootmultisource.service.impl.UserServiceImpl;
import top.lrshuai.SpringBootmultisource.service.impl.UserServiceImpl2;

@RequestMapping("/user")
@RestController
public class UserController {

	@Autowired
	private UserServiceImpl userService;
	@Autowired
	private UserServiceImpl2 userService2;
	
	/**
	 * num 为2 时，请求第二个数据源，否则为默认的第一个数据源
	 * @param num
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/getUserInfo/{num}")
	public RestBody getUserInfo(@PathVariable("num")String num, UserDto userDto) throws Exception{
		return num.equals("2")?userService2.getUserInfo(userDto):userService.getUserInfo(userDto);
	}
	
	/**
	 * num 为2 时，请求第二个数据源，否则为默认的第一个数据源
	 * @param num
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/getUserList/{num}")
	public RestBody getUserList(@PathVariable("num")String num) throws Exception{
		return num.equals("2")?userService2.getUserList():userService.getUserList();
	}
	
	/**
	 * 玩一下
	 * @param type
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/testExcetion")
	public RestBody testExcetion(String type) throws Exception{
		return userService.test(type);
	}
}

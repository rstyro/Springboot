package top.lrshuai.annotation.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import top.lrshuai.annotation.entity.User;
import top.lrshuai.annotation.mapper.UserMapper;

@RestController
public class UserController {

	@Autowired
	private UserMapper userMapper;
	
	@RequestMapping("/getAll")
	public Object getAllList() throws Exception{
		List<User> ulist = userMapper.getAll();
		System.out.println("ulist="+ulist);
		return ulist;
	}
	
	@RequestMapping("/user/{id}")
	public Object getUserById(@PathVariable("id") Long uid) throws Exception{
		System.out.println("uid"+uid);
		User u = userMapper.getUserById(uid);
		System.out.println("user="+u);
		return u == null ?"没有这个用户":u;
	}
	@RequestMapping("/save")
	public String saveUser() throws Exception{
		Map<String, Object> map = new HashMap<>();
		map.put("username", "userName"+UUID.randomUUID().toString().replaceAll("-", ""));
		map.put("nickName", "nick"+UUID.randomUUID().toString().replaceAll("-", ""));
		map.put("sex", new Random().nextInt(10) %2 == 1 ? "man":"woman");
		System.out.println("map="+map);
		int rest = userMapper.save(map);
		return rest == 1?"成功"+map:"失败";
	}
	
	@RequestMapping("/update")
	public String updateUser() throws Exception{
		Map<String, Object> map = new HashMap<>();
		map.put("username", "userName"+UUID.randomUUID().toString().replaceAll("-", ""));
		map.put("nickName", "nick"+UUID.randomUUID().toString().replaceAll("-", ""));
		map.put("id", new Random().nextInt(5));
		System.out.println("map="+map);
		int rest = userMapper.update(map);
		return rest == 1?"成功"+map:"失败";
	}
	
	
	@RequestMapping("/del/{id}")
	public String delUser (@PathVariable("id") Long id) throws Exception{
		int rest = userMapper.delete(id);
		return rest == 1?"成功":"失败";
	}
}

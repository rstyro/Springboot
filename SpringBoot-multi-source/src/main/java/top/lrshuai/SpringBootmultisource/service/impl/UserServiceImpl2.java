package top.lrshuai.SpringBootmultisource.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import top.lrshuai.SpringBootmultisource.dto.UserDto;
import top.lrshuai.SpringBootmultisource.entity.ApiResultEnum;
import top.lrshuai.SpringBootmultisource.entity.RestBody;
import top.lrshuai.SpringBootmultisource.mapper.UserMapper;
import top.lrshuai.SpringBootmultisource.service.UserService;

import com.alibaba.druid.util.StringUtils;

@Service
public class UserServiceImpl2 implements UserService{

	@Autowired
	private UserMapper userMapper;
	
	@Override
	public RestBody getUserInfo(UserDto userDto) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		System.out.println("userDto="+userDto);
		if(!StringUtils.isEmpty(userDto.getUsername())){
			map.put("username", userDto.getUsername());
		}
		if(!StringUtils.isEmpty(userDto.getUserId())){
			map.put("userId", userDto.getUserId());
		}
		return new RestBody(ApiResultEnum.SUCCESS, userMapper.getUserInfo(map));
	}

	@Override
	public RestBody getUserList() throws Exception{
		return new RestBody(ApiResultEnum.SUCCESS, userMapper.getUserList());
	}

}

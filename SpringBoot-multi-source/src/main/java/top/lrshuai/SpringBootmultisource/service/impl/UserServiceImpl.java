package top.lrshuai.SpringBootmultisource.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import top.lrshuai.SpringBootmultisource.dto.UserDto;
import top.lrshuai.SpringBootmultisource.entity.ApiResultEnum;
import top.lrshuai.SpringBootmultisource.entity.RestBody;
import top.lrshuai.SpringBootmultisource.exception.ApiException;
import top.lrshuai.SpringBootmultisource.mapper.UserMapper;
import top.lrshuai.SpringBootmultisource.service.UserService;

import com.alibaba.druid.util.StringUtils;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserMapper userMapper;
	
	@Override
	public RestBody getUserInfo(UserDto userDto) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
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
	public RestBody test(String type) throws Exception{
		switch(type){
			case "1":
				throw new ApiException(666, "你传的是1", null,null);
			case "2":
				throw new RuntimeException("不知道什么鬼");
			case "3":
				throw new NullPointerException();
			case "4":
				throw new ApiException(999, "这个是自定义异常，爱转啥就转啥", null,null);
		}
		return new RestBody(ApiResultEnum.SUCCESS, null);
	}

}

package top.lrshuai.SpringBootmultisource.service;


import top.lrshuai.SpringBootmultisource.dto.UserDto;
import top.lrshuai.SpringBootmultisource.entity.RestBody;

public interface UserService {
	public RestBody getUserInfo(UserDto userDto) throws Exception;
	public RestBody getUserList() throws Exception;
	
}

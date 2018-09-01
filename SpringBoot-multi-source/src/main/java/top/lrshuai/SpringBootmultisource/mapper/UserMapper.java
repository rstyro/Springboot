package top.lrshuai.SpringBootmultisource.mapper;

import java.util.List;
import java.util.Map;

import top.lrshuai.SpringBootmultisource.entity.User;

public interface UserMapper {
	public List<User> getUserList();
	public User getUserInfo(Map<String,Object> map);
}

package top.lrshuai.xml.mapper;

import java.util.List;
import java.util.Map;

import top.lrshuai.xml.entity.User;

public interface UserMapper {
	public List<User> getAll();
	public User getUserById(long id);
	public int save(Map<String,Object> map);
	public int update(Map<String,Object> map);
	public int delete(long id);
}

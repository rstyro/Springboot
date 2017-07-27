package top.lrshuai.annotation.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import top.lrshuai.annotation.entity.User;

public interface UserMapper {
	
	@Select("SELECT * FROM users")
	@Results({
		@Result(property = "sex",  column = "sex"),
		@Result(property = "username",  column = "username"),
		@Result(property = "nickName", column = "nick_name")
	})
	public List<User> getAll();
	
	
	@Select("SELECT * FROM users where id= #{id}")
	@Results({
		@Result(property = "sex",  column = "sex"),
		@Result(property = "username",  column = "username"),
		@Result(property = "nickName", column = "nick_name")
	})
	public User getUserById(long id);
	
	@Insert("INSERT INTO users(username,nick_name,sex) VALUES(#{username}, #{nickName}, #{sex})")
	public int save(Map<String,Object> map);
	
	@Update("UPDATE users SET username=#{username},nick_name=#{nickName} WHERE id =#{id}")
	public int update(Map<String,Object> map);
	
	@Delete("DELETE FROM users WHERE id =#{id}")
	public int delete(long id);
}

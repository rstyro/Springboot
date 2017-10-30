package top.lrshuai.mongodb.dao;

import java.util.List;

import top.lrshuai.mongodb.entity.User;
/**
 * 
 * @author rstyro
 *
 */
public interface UserDao {
	public void saveUser(User user);
	public void saveBathUser(List<User> users);
	public void delUserById(Long id);
	public int upadteUserById(User user);
	public User findUserByName(String name);
	public List<User> findAll();
	public List<User> findUserByLikeName(String name);
	
}

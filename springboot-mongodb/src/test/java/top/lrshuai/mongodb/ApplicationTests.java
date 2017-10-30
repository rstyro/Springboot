package top.lrshuai.mongodb;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import top.lrshuai.mongodb.dao.UserDao;
import top.lrshuai.mongodb.entity.User;
import top.lrshuai.mongodb.repository.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private UserRepository userRepository;
	
	/**
	 * 批量新增
	 */
	@Test
	public void customBathSave() {
		List<User> users = new ArrayList<>();
		User u1 = new User(1l, "lrshuai", 23);
		User u2 = new User(2l, "rstyro", 24);
		User u3 = new User(3l, "tyro", 25);
		User u4 = new User(4l, "mongo", 26);
		users.add(u1);
		users.add(u2);
		users.add(u3);
		users.add(u4);
		userDao.saveBathUser(users);
	}
	
	/**
	 * 保存单个，存在则修改
	 */
	@Test
	public void customSave() {
		userDao.saveUser(new User(1l, "rstyro", 23));
	}
	
	/**
	 * 删除
	 */
	@Test
	public void customDel() {
		userDao.delUserById(1l);
	}
	
	/**
	 * 更新通过ID
	 */
	@Test
	public void customUpdate() {
		User user  = new User(2l,"修改用户名",25);
		System.out.println(userDao.upadteUserById(user));
	}
	
	/**
	 * 通过用户名精确查找
	 */
	@Test
	public void customQuery() {
		User user = userDao.findUserByName("haha");
		System.out.println("user="+user);
	}
	
	@Test
	public void customQueryAll() {
		List<User> users = userDao.findAll();
		System.out.println("users="+users);
	}
	
	/**
	 * 通过name 模糊查找
	 */
	@Test
	public void customQueryLikeName() {
		String name="o";
		List<User> users = userDao.findUserByLikeName(name);
		System.out.println("users="+users);
	}
	

	/********** 下面是继承 MongoRepository 的方法 ******/
	@Test
	public void saveTest(){
		User user = userRepository.save(new User(2l, "haha", 23));
		System.out.println("保存后返回的 user"+user);
	}
	
	@Test
	public void delTest(){
		userRepository.delete(3l);
	}
	
	@Test
	public void updateTest(){
		User user = userRepository.save(new User(4l, "测试", 24));
		System.out.println("修改后返回的 user"+user);
	}
	
	@Test
	public void findOneByNameTest(){
		User u = userRepository.findUserByName("rstyro");
		System.out.println("user="+u);
	}
	
	@Test
	public void findAllTest(){
		List<User> users = userRepository.findAll();
		System.out.println("users="+users);
	}
	
}

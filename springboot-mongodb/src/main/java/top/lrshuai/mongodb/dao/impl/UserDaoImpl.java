package top.lrshuai.mongodb.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.mongodb.WriteResult;

import top.lrshuai.mongodb.dao.UserDao;
import top.lrshuai.mongodb.entity.User;

@Component
public class UserDaoImpl implements UserDao{

	@Autowired
	private MongoTemplate mongoTemplate;
	
	/**
	 * 新增一个，存在则覆盖
	 */
	@Override
	public void saveUser(User user) {
		mongoTemplate.save(user);
	}
	
	/**
	 * 批量新增
	 */
	@Override
	public void saveBathUser(List<User> users) {
		mongoTemplate.insert(users, User.class);
	}
	
	/**
	 * 删除
	 */
	@Override
	public void delUserById(Long id) {
		Query query = new Query(Criteria.where("id").is(id));
		mongoTemplate.remove(query, User.class);
	}

	/**
	 * 更新 通过id
	 */
	@Override
	public int upadteUserById(User user) {
		Query query = new Query(Criteria.where("id").is(user.getId()));
		Update update = new Update();
		update.set("name", user.getName()).set("age", user.getAge());
		WriteResult result =  mongoTemplate.updateFirst(query, update, User.class);
		return result.getN();
	}

	/**
	 * 通过名称查找
	 */
	@Override
	public User findUserByName(String name) {
		Query query = new Query(Criteria.where("name").is(name));
		return mongoTemplate.findOne(query, User.class);
	}
	
	/**
	 * 查询所有
	 */
	@Override
	public List<User> findAll() {
		return mongoTemplate.findAll(User.class);
	}
	
	/**
	 * name模糊查找
	 */
	@Override
	public List<User> findUserByLikeName(String name) {
		Query query = new Query();
		query.addCriteria(Criteria.where("name").regex(".*" +name+ ".*"));
		return mongoTemplate.find(query, User.class);
	}

}

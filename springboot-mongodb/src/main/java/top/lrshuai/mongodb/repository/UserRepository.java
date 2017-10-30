package top.lrshuai.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import top.lrshuai.mongodb.entity.User;

@Component
public interface UserRepository extends MongoRepository<User, Long>{
	public User findUserByName(String username);

}

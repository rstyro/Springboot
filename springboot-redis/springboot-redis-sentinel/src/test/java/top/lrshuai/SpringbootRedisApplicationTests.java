package top.lrshuai;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import top.lrshuai.entity.User;


@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootRedisApplicationTests {


	@Autowired
	private RedisTemplate<String, Object> redisTemplate;


	@Test
	public void test() throws Exception {

		// 保存对象
		User user = new User("C++", 40);
		redisTemplate.opsForValue().set(user.getUsername(), user);

		user = new User("Java", 30);
		redisTemplate.opsForValue().set(user.getUsername(), user);

		user = new User("Python", 20);
		redisTemplate.opsForValue().set(user.getUsername(), user);
		System.out.println(JSON.toJSONString(redisTemplate.opsForValue().get("C++")));
		System.out.println(JSON.toJSONString(redisTemplate.opsForValue().get("Java")));
		System.out.println(JSON.toJSONString(redisTemplate.opsForValue().get("Python")));

	}

}

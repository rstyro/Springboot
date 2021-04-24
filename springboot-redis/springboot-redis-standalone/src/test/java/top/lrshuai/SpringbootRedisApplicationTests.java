package top.lrshuai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import top.lrshuai.entity.User;


@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootRedisApplicationTests {

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Autowired
	private RedisTemplate<String, User> redisTemplate;
	
	@Autowired
	private RedisTemplate<String, List<User>> rt;
	
	
	@Autowired
	private RedisTemplate<String, List<Map<String,Object>>> rm;

	@Test
	public void test() throws Exception {

		// 保存字符串
		stringRedisTemplate.opsForValue().set("url", "www.lrshuai.top");
		Assert.assertEquals("www.lrshuai.top", stringRedisTemplate.opsForValue().get("url"));

		// 保存对象
		User user = new User("C++", 40);
		redisTemplate.opsForValue().set(user.getUsername(), user);

		user = new User("Java", 30);
		redisTemplate.opsForValue().set(user.getUsername(), user);

		user = new User("Python", 20);
		redisTemplate.opsForValue().set(user.getUsername(), user);

		Assert.assertEquals(20, redisTemplate.opsForValue().get("Python").getAge());
		Assert.assertEquals(30, redisTemplate.opsForValue().get("Java").getAge());
		Assert.assertEquals(40, redisTemplate.opsForValue().get("C++").getAge());

	}
	
	@Test
	public void test1() throws Exception{
		List<User> us = new ArrayList<>();
		User u1 = new User("rs1", 21);
		User u2 = new User("rs2", 22);
		User u3 = new User("rs3", 23);
		us.add(u1);
		us.add(u2);
		us.add(u3);
		rt.opsForValue().set("key_ul", us);
		System.out.println("放入缓存》。。。。。。。。。。。。。。。。。。。");
		System.out.println("=============================");
		List<User> redisList = rt.opsForValue().get("key_ul");
		System.out.println("redisList="+redisList);
	}
	
	@Test
	public void test2() throws Exception{
		List<Map<String,Object>> ms = new ArrayList<>();
		Map<String,Object> map = new HashMap<>();
		map.put("name", "rs");
		map.put("age", 20);
		
		Map<String,Object> map1 = new HashMap<>();
		map1.put("name", "rs1");
		map1.put("age", 21);
		
		Map<String,Object> map2 = new HashMap<>();
		map2.put("name", "rs2");
		map2.put("age", 22);
		
		ms.add(map);
		ms.add(map1);
		ms.add(map2);
		rm.opsForValue().set("key_ml", ms);
		System.out.println("放入缓存》。。。。。。。。。。。。。。。。。。。");
		System.out.println("=============================");
		List<Map<String,Object>> mls = rm.opsForValue().get("key_ml");
		System.out.println("mls="+mls);
	}

}

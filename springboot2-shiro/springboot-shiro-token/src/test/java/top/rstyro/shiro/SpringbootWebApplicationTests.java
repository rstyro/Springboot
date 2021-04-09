package top.rstyro.shiro;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootWebApplicationTests {

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Test
	public void contextLoads() {
		String oneKey = "aaa";
		String sendKey = "bbb";
		Boolean isHas = stringRedisTemplate.opsForHash().hasKey(oneKey, sendKey);
		System.out.println("isHas="+isHas);
//		stringRedisTemplate.opsForHash().put(oneKey,sendKey,"110");
//		isHas = stringRedisTemplate.opsForHash().hasKey(oneKey, sendKey);
//		System.out.println("isHas="+isHas);
	}

}

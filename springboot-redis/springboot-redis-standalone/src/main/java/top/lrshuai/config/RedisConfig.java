package top.lrshuai.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;

import redis.clients.jedis.JedisPoolConfig;

/**
 * 
 * @author tyro
 *
 */
@Configuration
public class RedisConfig {
	
	@Value("${spring.redis.database}")
	private int database;
	
	@Value("${spring.redis.host}")
	private String host;
	
	@Value("${spring.redis.port}")
	private int port;
	
	@Value("${spring.redis.timeout}")
	private int timeout;
	
	@Value("${spring.redis.pool.max-idle}")
	private int maxidle;
	
	@Value("${spring.redis.pool.min-idle}")
	private int minidle;
	
	@Value("${spring.redis.pool.max-active}")
	private int maxActive;
	
	@Value("${spring.redis.pool.max-wait}")
	private long maxWait;
	
    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
    	JedisPoolConfig config = new JedisPoolConfig(); 
//    	最大空闲连接数, 默认8个
        config.setMaxIdle(maxidle);
//      最小空闲连接数, 默认0
        config.setMinIdle(minidle);
//      最大连接数, 默认8个
        config.setMaxTotal(maxActive);
//      获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1
        config.setMaxWaitMillis(maxWait);
        
    	JedisConnectionFactory factory = new JedisConnectionFactory();
    	factory.setDatabase(database);
        factory.setHostName(host);
        factory.setPort(port);
        factory.setTimeout(timeout);
        factory.setPoolConfig(config);
        return factory;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Bean
    public RedisTemplate<String, ?> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, ?> template = new RedisTemplate();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new RedisObjectSerializer());
        return template;
    }


}

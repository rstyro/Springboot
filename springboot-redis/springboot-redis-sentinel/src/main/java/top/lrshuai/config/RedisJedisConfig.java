package top.lrshuai.config;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 *
 * @author rstyro
 * jedis 配置
 */
@Configuration
public class RedisJedisConfig {

    @Value("${spring.redis.sentinel.password}")
    private String password;

    @Value("${spring.redis.sentinel.master}")
    private String masterName;

    @Value("${spring.redis.sentinel.nodes}")
    private String[] sentinels;

    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        // 哨兵配置
        RedisSentinelConfiguration redisSentinelConfiguration = new RedisSentinelConfiguration();
        redisSentinelConfiguration.setMaster(masterName);
        for (String sentinel : sentinels) {
            String[] arr = sentinel.split(":");
            redisSentinelConfiguration.sentinel(new RedisNode(arr[0], Integer.parseInt(arr[1])));
        }
        redisSentinelConfiguration.setPassword(RedisPassword.of(password));
        return new JedisConnectionFactory(redisSentinelConfiguration);
    }

    @Bean
    public RedisTemplate<String,Object> redisTemplate(JedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String,Object> template = new RedisTemplate<>();
        //使用fastjson序列化
        FastJsonRedisSerializer fastJsonRedisSerializer = new FastJsonRedisSerializer(Object.class);
        // value值的序列化采用fastJsonRedisSerializer
        template.setValueSerializer(fastJsonRedisSerializer);
        template.setHashValueSerializer(fastJsonRedisSerializer);
        // key的序列化采用StringRedisSerializer
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

}

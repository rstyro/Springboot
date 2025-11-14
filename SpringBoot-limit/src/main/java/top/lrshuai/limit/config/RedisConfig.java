package top.lrshuai.limit.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.List;

/**
 * 
 * @author rstyro
 * @time 2017-07-31
 *
 */
@Configuration
public class RedisConfig{

    /**
     * 令牌桶-lua脚本
     */
    @Value("classpath:lua/tokenRate.lua")
    private Resource tokenLuaFile;

    /**
     * 漏牌-lua脚本
     */
    @Value("classpath:lua/leakyBucket.lua")
    private Resource leakyLuaFile;

    /**
     * 漏牌-lua脚本
     */
    @Value("classpath:lua/slidingWindow.lua")
    private Resource slidingWindowLuaFile;

    /**
     * 令牌桶限流 Lua 脚本
     */
    @Bean
    public RedisScript<List> tokenBucketScript() {
        DefaultRedisScript<List> redisScript = new DefaultRedisScript<>();
        redisScript.setLocation(tokenLuaFile);
        redisScript.setResultType(List.class);
        return redisScript;
    }

    /**
     * 漏桶限流 Lua 脚本
     */
    @Bean
    public RedisScript<Long> leakyBucketScript() {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setLocation(leakyLuaFile);
        redisScript.setResultType(Long.class);
        return redisScript;
    }

    /**
     * 滑动时间窗口计数器限流 Lua 脚本
     */
    @Bean
    public RedisScript<Long> slidingWindowScript() {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setLocation(slidingWindowLuaFile);
        redisScript.setResultType(Long.class);
        return redisScript;
    }

    /**
     * RedisTemplate配置
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        // 设置序列化
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(
                Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        // 配置redisTemplate
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        RedisSerializer<?> stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);// key序列化
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);// value序列化
        redisTemplate.setHashKeySerializer(stringSerializer);// Hash key序列化
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);// Hash value序列化
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }



}

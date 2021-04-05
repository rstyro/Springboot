package top.rstyro.shiro.shiro;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.data.redis.core.RedisTemplate;
import top.rstyro.shiro.utils.ApplicationContextUtils;

import java.util.Collection;
import java.util.Set;

public class ShiroRedisCache<K,V> implements Cache<K,V> {

    private String cacheName;


    public ShiroRedisCache() {
    }

    public ShiroRedisCache(String cacheName) {
        this.cacheName = cacheName;
    }


    @Override
    public V get(K k) throws CacheException {
        System.out.println("====get===");
        return (V) getRedisTemplate().opsForHash().get(this.cacheName,k.toString());
    }

    @Override
    public V put(K k, V v) throws CacheException {
        System.out.println("====put===");
        getRedisTemplate().opsForHash().put(this.cacheName,k.toString(),v);
        return null;
    }

    @Override
    public V remove(K k) throws CacheException {
        System.out.println("====remove===");
        return (V) getRedisTemplate().opsForHash().delete(this.cacheName,k.toString());
    }

    @Override
    public void clear() throws CacheException {
          System.out.println("====clear===");
        getRedisTemplate().delete(this.cacheName);
    }

    @Override
    public int size() {
          System.out.println("====size===");
        return getRedisTemplate().opsForHash().size(this.cacheName).intValue();
    }

    @Override
    public Set<K> keys() {
          System.out.println("====keys===");
        return getRedisTemplate().opsForHash().keys(this.cacheName);
    }

    @Override
    public Collection<V> values() {
          System.out.println("====values===");
        return getRedisTemplate().opsForHash().values(this.cacheName);
    }


    private RedisTemplate getRedisTemplate(){
        RedisTemplate redisTemplate = (RedisTemplate) ApplicationContextUtils.getBean("shiroRedisTemplate");
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }
}

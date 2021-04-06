package top.rstyro.shiro.shiro;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.data.redis.core.RedisTemplate;
import top.rstyro.shiro.utils.ApplicationContextUtils;

import java.util.Collection;
import java.util.Set;

/**
 * shiro之 redis 缓存
 * @param <K>
 * @param <V>
 */
public class ShiroRedisCache<K,V> implements Cache<K,V> {

    private String cacheName;


    public ShiroRedisCache() {
    }

    public ShiroRedisCache(String cacheName) {
        this.cacheName = cacheName;
    }


    @Override
    public V get(K k) throws CacheException {
        return (V) getRedisTemplate().opsForHash().get(this.cacheName,k.toString());
    }

    @Override
    public V put(K k, V v) throws CacheException {
        getRedisTemplate().opsForHash().put(this.cacheName,k.toString(),v);
        return null;
    }

    @Override
    public V remove(K k) throws CacheException {
        return (V) getRedisTemplate().opsForHash().delete(this.cacheName,k.toString());
    }

    @Override
    public void clear() throws CacheException {
        getRedisTemplate().delete(this.cacheName);
    }

    @Override
    public int size() {
        return getRedisTemplate().opsForHash().size(this.cacheName).intValue();
    }

    @Override
    public Set<K> keys() {
        return getRedisTemplate().opsForHash().keys(this.cacheName);
    }

    @Override
    public Collection<V> values() {
        return getRedisTemplate().opsForHash().values(this.cacheName);
    }


    private RedisTemplate getRedisTemplate(){
        return (RedisTemplate) ApplicationContextUtils.getBean("shiroRedisTemplate");
    }
}

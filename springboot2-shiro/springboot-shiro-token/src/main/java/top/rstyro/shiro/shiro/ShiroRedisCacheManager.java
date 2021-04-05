package top.rstyro.shiro.shiro;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;

/**
 * shiro 自定义缓存管理器
 */
public class ShiroRedisCacheManager implements CacheManager {

    @Override
    public <K, V> Cache<K, V> getCache(String cacheName) throws CacheException {
        return new ShiroRedisCache(cacheName);
    }
}

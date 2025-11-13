package top.lrshuai.limit.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class TokenBucketRateLimiter {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedisScript<List> tokenBucketScript;

    /**
     * 尝试获取令牌
     *
     * @param key          限流key
     * @param rate         令牌生成速率 (个/秒)
     * @param capacity     桶容量
     * @param tokenRequest 请求的令牌数
     * @return 是否允许访问
     */
    public boolean tryAcquire(String key, double rate, int capacity, int tokenRequest) {
        // 转换为秒
        long now = System.currentTimeMillis() / 1000;

        List<String> keys = Arrays.asList(key);

        @SuppressWarnings("unchecked")
        List<Long> result = (List<Long>) redisTemplate.execute(tokenBucketScript, keys, rate, capacity, now, tokenRequest);

        if (ObjectUtils.isEmpty(result)) {
            log.warn("令牌桶限流脚本执行异常, key: {}", key);
            return false;
        }

        boolean allowed = result.get(0) == 1;
        long remainingTokens = result.get(1);
        long bucketCapacity = result.get(2);

        if (log.isDebugEnabled()) {
            log.debug("限流检查 - key: {}, 允许: {}, 剩余令牌: {}/{}, 请求令牌数: {}",
                    key, allowed, remainingTokens, bucketCapacity, tokenRequest);
        }

        return allowed;
    }

    /**
     * 简化方法 - 默认请求1个令牌
     */
    public boolean tryAcquire(String key, double rate, int capacity) {
        return tryAcquire(key, rate, capacity, 1);
    }
}

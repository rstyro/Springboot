package top.lrshuai.limit.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@Service
public class LeakyBucketRateLimiter {


    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedisScript<Long> leakyBucketScript;

    /**
     * 尝试获取通行证
     * @param key 限流key
     * @param capacity 桶容量
     * @param rate 流出速率（每秒请求数）
     * @param requestCount 请求数量
     * @return true-允许访问，false-被限流
     */
    public boolean tryAcquire(String key, int capacity, int rate, int requestCount) {
        long now = System.currentTimeMillis() / 1000; // 使用秒级时间戳

        Long result = redisTemplate.execute(leakyBucketScript,
                Collections.singletonList(key),
                capacity,rate,now,requestCount);

        return result != null && result == 1;
    }

    /**
     * 获取桶的当前状态（用于监控和调试）
     */
    public BucketStatus getBucketStatus(String key) {
        try {
            // 使用 RedisTemplate 的哈希操作获取值
            Object waterObj = redisTemplate.opsForHash().get(key, "water");
            Object lastLeakTimeObj = redisTemplate.opsForHash().get(key, "lastLeakTime");
            Long ttl = redisTemplate.getExpire(key);

            long water = 0;
            long lastLeakTime = 0;

            // 转换值
            if (waterObj != null) {
                water = Long.parseLong(waterObj.toString());
            }
            if (lastLeakTimeObj != null) {
                lastLeakTime = Long.parseLong(lastLeakTimeObj.toString());
            }

            return new BucketStatus(
                    water,
                    lastLeakTime,
                    ttl != null ? ttl : -2
            );
        } catch (Exception e) {
            log.error("Failed to get bucket status for key: {}", key, e);
            return new BucketStatus(0, 0, -2);
        }
    }


    /**
     * 清理限流数据
     */
    public void reset(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 桶状态信息
     */
    @Data
    @AllArgsConstructor
    public static class BucketStatus {
        /**
         * 当前桶中积压的请求数量
         * 这个值表示漏桶中当前有多少个"水单位"，每个水单位代表一个待处理的请求
         * 当有请求进入系统时，currentWater 会增加
         * 随着时间推移，水会以恒定速率从桶底漏出，currentWater 会相应减少
         * 如果 currentWater >= capacity（桶容量），新的请求会被拒绝
         */
        private long currentWater;
        /**
         * 最后一次计算漏水的时间戳
         * 用于计算从上次漏水到现在应该漏掉多少水
         * 计算公式：漏水量 = (当前时间 - lastLeakTime) × 流出速率
         */
        private long lastLeakTime;
        /**
         * Redis 中该限流 key 的剩余生存时间（单位：秒）
         * 表示这个限流桶还有多少秒会被 Redis 自动删除
         */
        private long ttl;
    }
}

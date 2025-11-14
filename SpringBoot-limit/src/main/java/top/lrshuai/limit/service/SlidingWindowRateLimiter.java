package top.lrshuai.limit.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SlidingWindowRateLimiter {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedisScript<Long> slidingWindowScript;

    /**
     * 尝试获取通行证
     * @param key 限流key
     * @param window 时间窗口大小（秒）
     * @param maxCount 时间窗口内允许的最大请求数
     * @param requestCount 请求数量
     * @return true-允许访问，false-被限流
     */
    public boolean tryAcquire(String key, int window, int maxCount, int requestCount) {
        long now = System.currentTimeMillis() / 1000; // 使用秒级时间戳
        Long result = redisTemplate.execute(slidingWindowScript,
                Collections.singletonList(key), window, maxCount, now, requestCount);

        return result != null && result == 1;
    }

    /**
     * 获取时间窗口的当前状态
     */
    public WindowStatus getWindowStatus(String key, int window) {
        try {
            long now = System.currentTimeMillis() / 1000;
            long windowStart = now - window;

            // 获取时间窗口内的请求总数
            Long count = redisTemplate.opsForZSet().count(key, windowStart, Double.MAX_VALUE);

            // 获取最早和最晚的请求时间
            Set<Object> members = redisTemplate.opsForZSet().range(key, 0, -1);
            long earliestTime = 0;
            long latestTime = 0;

            if (members != null && !members.isEmpty()) {
                List<Long> times = members.stream()
                        .map(member -> Long.parseLong(member.toString()) / 1000) // 转回秒级
                        .sorted()
                        .collect(Collectors.toList());

                earliestTime = times.get(0);
                latestTime = times.get(times.size() - 1);
            }
            Long ttl = redisTemplate.getExpire(key);
            return new WindowStatus(
                    count != null ? count : 0,
                    windowStart,
                    now,
                    earliestTime,
                    latestTime,
                    ttl != null ? ttl : -2
            );
        } catch (Exception e) {
            log.error("Failed to get window status for key: {}", key, e);
            return new WindowStatus(0, 0, 0, 0, 0, -2);
        }
    }

    /**
     * 清理限流数据
     */
    public void reset(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 时间窗口状态信息
     */
    @Data
    @AllArgsConstructor
    public static class WindowStatus {
        /**
         * 当前时间窗口内的请求数量
         */
        private long currentCount;

        /**
         * 时间窗口起始时间戳（秒）
         */
        private long windowStart;

        /**
         * 当前时间戳（秒）
         */
        private long currentTime;

        /**
         * 时间窗口内最早的请求时间戳（秒）
         */
        private long earliestRequestTime;

        /**
         * 时间窗口内最晚的请求时间戳（秒）
         */
        private long latestRequestTime;

        /**
         * key的剩余生存时间（秒）
         */
        private long ttl;

        @Override
        public String toString() {
            return String.format(
                    "WindowStatus{currentCount=%d, windowStart=%d, currentTime=%d, " +
                            "earliestRequest=%d, latestRequest=%d, ttl=%d}",
                    currentCount, windowStart, currentTime,
                    earliestRequestTime, latestRequestTime, ttl
            );
        }
    }

}

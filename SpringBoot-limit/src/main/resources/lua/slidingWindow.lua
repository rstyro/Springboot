-- 滑动时间窗口计数器限流算法

-- 参数说明：
-- KEYS[1]: 限流的key
-- ARGV[1]: 时间窗口大小（秒）
-- ARGV[2]: 时间窗口内允许的最大请求数
-- ARGV[3]: 当前时间戳（秒）
-- ARGV[4]: 本次请求数量（默认为1）

local key = KEYS[1]
local window = tonumber(ARGV[1])
local maxCount = tonumber(ARGV[2])
local now = tonumber(ARGV[3])
local requestCount = tonumber(ARGV[4]) or 1

-- 计算时间窗口的起始时间戳
local windowStart = now - window

-- 移除时间窗口之前的数据
redis.call('zremrangebyscore', key, 0, windowStart)

-- 获取当前时间窗口内的请求总数
local currentCount = redis.call('zcard', key)

-- 检查是否超过限制
if currentCount + requestCount <= maxCount then
    -- 没有超过限制，添加当前请求
    for i = 1, requestCount do
        -- 使用毫秒级时间戳+随机数确保成员唯一性
        local member = now * 1000 + math.random(0, 999)
        redis.call('zadd', key, member, member)
    end

    -- 设置key的过期时间为窗口大小+1秒，确保数据自动清理
    redis.call('expire', key, window + 1)

    return 1  -- 允许访问
else
    -- 超过限制，拒绝请求
    return 0  -- 被限流
end
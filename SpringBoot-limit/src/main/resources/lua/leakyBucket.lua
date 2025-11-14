-- 漏桶限流算法 Lua 脚本

-- 参数说明：
-- KEYS[1]: 限流的key
-- ARGV[1]: 桶的容量
-- ARGV[2]: 流出速率（每秒处理数）
-- ARGV[3]: 当前时间戳（秒）
-- ARGV[4]: 本次请求数量

local key = KEYS[1]
local capacity = tonumber(ARGV[1])
local rate = tonumber(ARGV[2])
local now = tonumber(ARGV[3])
local requestCount = tonumber(ARGV[4])

-- 获取桶的当前状态
local bucketInfo = redis.call('hmget', key, 'water', 'lastLeakTime')
local currentWater = 0
local lastLeakTime = now

-- 如果桶存在，获取当前水量和上次漏水时间
if bucketInfo[1] then
    currentWater = tonumber(bucketInfo[1])
end

if bucketInfo[2] then
    lastLeakTime = tonumber(bucketInfo[2])
end

-- 计算从上次漏水到现在的漏出量
local leakAmount = (now - lastLeakTime) * rate
if leakAmount > 0 then
    currentWater = math.max(0, currentWater - leakAmount)
    lastLeakTime = now
end

-- 检查桶是否有足够空间容纳新请求
if currentWater + requestCount <= capacity then
    -- 允许请求，更新桶状态
    currentWater = currentWater + requestCount
    redis.call('hmset', key, 'water', currentWater, 'lastLeakTime', lastLeakTime)
    redis.call('expire', key, 3600)  -- 设置过期时间，防止内存泄漏
    return 1  -- 允许访问
else
    -- 桶已满，拒绝请求
    return 0  -- 被限流
end
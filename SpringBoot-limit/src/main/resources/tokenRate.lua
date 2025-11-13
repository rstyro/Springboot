-- 令牌桶限流 Lua 脚本
-- KEYS[1]: 限流的key
-- ARGV[1]: 令牌生成速率 (每秒生成的令牌数)
-- ARGV[2]: 桶的容量 (最大令牌数)
-- ARGV[3]: 当前时间戳 (秒)
-- ARGV[4]: 本次请求的令牌数 (默认为1)

local key = KEYS[1]
local rate = tonumber(ARGV[1])
local capacity = tonumber(ARGV[2])
local now = tonumber(ARGV[3])
local requested = tonumber(ARGV[4])

-- 计算填满桶需要的时间，用于设置key的过期时间
local fill_time = capacity / rate
local ttl = math.floor(fill_time * 2)  -- 过期时间为填满时间的2倍

-- 从Redis获取上次的令牌数和刷新时间
local last_tokens = tonumber(redis.call("get", key))
if last_tokens == nil then
    last_tokens = capacity  -- 第一次访问，令牌数为桶容量
end

local last_refreshed = tonumber(redis.call("get", key .. ":ts"))
if last_refreshed == nil then
    last_refreshed = now  -- 第一次访问，刷新时间为当前时间
end

-- 计算时间差和应该补充的令牌数
local delta = math.max(0, now - last_refreshed)
local filled_tokens = math.min(capacity, last_tokens + (delta * rate))

-- 判断是否允许本次请求
local allowed = filled_tokens >= requested
local new_tokens = filled_tokens
local allowed_num = 0

if allowed then
    new_tokens = filled_tokens - requested
    allowed_num = 1
    -- 更新令牌数和时间戳
    redis.call("setex", key, ttl, new_tokens)
    redis.call("setex", key .. ":ts", ttl, now)
else
    -- 即使不允许，也更新状态（为了计算下一次的令牌数）
    redis.call("setex", key, ttl, new_tokens)
    redis.call("setex", key .. ":ts", ttl, last_refreshed)
end

-- 返回结果：是否允许(1/0)，剩余令牌数，桶容量
return {allowed_num, new_tokens, capacity}
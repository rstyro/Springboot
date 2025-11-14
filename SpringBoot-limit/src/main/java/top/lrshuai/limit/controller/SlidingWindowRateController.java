package top.lrshuai.limit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.lrshuai.limit.annotation.SlidingWindowLimit;
import top.lrshuai.limit.common.R;
import top.lrshuai.limit.service.SlidingWindowRateLimiter;

@RestController
@RequestMapping("/SlidingWindowRate")
public class SlidingWindowRateController {

    @Autowired
    private SlidingWindowRateLimiter slidingWindowRateLimiter;

    /**
     * 测试
     */
    @GetMapping("/test1")
    @SlidingWindowLimit(window = 3, maxCount = 1)
    public R test1() {
        //TODO ...
        return R.ok();
    }

    @GetMapping("/test2")
    @SlidingWindowLimit(key = "sliding_window:test2",window = 60, maxCount = 5)
    public R test2() {
        //TODO ...
        return R.ok();
    }

    @SlidingWindowLimit(key = "'user  :' + #username")
    @GetMapping("/search")
    public R search(@RequestParam String username) {
        // 搜索逻辑 - 这里key会根据username动态变化
        return R.ok("username:" + username);
    }

    @GetMapping("/status/{key}")
    public R getStatus(@PathVariable String key, int window) {
        return R.ok(slidingWindowRateLimiter.getWindowStatus(key, window));
    }

    @PostMapping("/reset/{key}")
    public R reset(@PathVariable String key) {
        slidingWindowRateLimiter.reset(key);
        return R.ok();
    }
}

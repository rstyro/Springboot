package top.lrshuai.limit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.lrshuai.limit.annotation.LeakyBucketLimit;
import top.lrshuai.limit.common.R;
import top.lrshuai.limit.service.LeakyBucketRateLimiter;

@RestController
@RequestMapping("/leakyRate")
public class LeakyRateController {

    @Autowired
    private LeakyBucketRateLimiter leakyBucketRateLimiter;


    /**
     * 测试
     */
    @GetMapping("/test1")
    @LeakyBucketLimit(rate = 1, capacity = 3)
    public R test1() {
        //TODO ...
        return R.ok();
    }

    @GetMapping("/test2")
    @LeakyBucketLimit(key = "leaky_rate:test2",rate = 2, capacity = 1)
    public R test2() {
        //TODO ...
        return R.ok();
    }

    @LeakyBucketLimit(key = "'user  :' + #username", rate = 1, capacity = 5)
    @GetMapping("/search")
    public R search(@RequestParam String username) {
        // 搜索逻辑 - 这里key会根据username动态变化
        return R.ok("username:" + username);
    }

    @GetMapping("/status/{key}")
    public R getStatus(@PathVariable String key) {
        return R.ok(leakyBucketRateLimiter.getBucketStatus( key));
    }

    @PostMapping("/reset/{key}")
    public R reset(@PathVariable String key) {
        leakyBucketRateLimiter.reset(key);
        return R.ok();
    }
}

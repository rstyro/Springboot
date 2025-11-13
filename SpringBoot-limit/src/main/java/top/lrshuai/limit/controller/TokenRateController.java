package top.lrshuai.limit.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.lrshuai.limit.annotation.TokenBucketRateLimit;
import top.lrshuai.limit.common.R;

@RestController
@RequestMapping("/tokenRate")
public class TokenRateController {

    /**
     * 测试发送短信
     */
    @GetMapping("/sendSms")
    @TokenBucketRateLimit(rate = 0.1, capacity = 2, message = "短信发送过于频繁")
    public R sendSms(){
        //TODO ...
        return R.ok();
    }

    /**
     * “@TokenBucketRateLimit(rate = 5.0, capacity = 20)”  每秒5次，突发20次
     */
    @TokenBucketRateLimit(key = "'search:' + #keyword", rate = 5.0, capacity = 10)
    @GetMapping("/search")
    public R search(@RequestParam String keyword) {
        // 搜索逻辑 - 这里key会根据keyword动态变化
        return R.ok("搜索结果:"+keyword);
    }
}

package top.lrshuai.limit.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.lrshuai.limit.annotation.RedissonRateLimit;
import top.lrshuai.limit.common.R;

@RestController
@RequestMapping("/redissonRate")
public class RedissonRateController {

    @GetMapping("/queryQuotaInfo")
    @RedissonRateLimit(key = "'queryQuotaInfo:' + #storageType",rate = 1)
    public R queryQuotaInfo(@RequestParam(value = "storageType") String storageType) {
        return R.ok("storageType:"+storageType);
    }
}

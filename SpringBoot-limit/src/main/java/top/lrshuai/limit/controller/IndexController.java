package top.lrshuai.limit.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.lrshuai.limit.annotation.RequestLimit;
import top.lrshuai.limit.common.Result;

@RestController
@RequestMapping("/index")
@RequestLimit(maxCount = 5,second = 1)
public class IndexController {

    /**
     * @RequestLimit 修饰在方法上，优先使用其参数
     * @return
     */
    @GetMapping("/test1")
    @RequestLimit
    public Result test(){
        //TODO ...
        return Result.ok();
    }

    /**
     * @RequestLimit 修饰在类上，用的是类的参数
     * @return
     */
    @GetMapping("/test2")
    public Result test2(){
        //TODO ...
        return Result.ok();
    }
}

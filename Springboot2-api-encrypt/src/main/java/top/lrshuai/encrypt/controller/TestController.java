package top.lrshuai.encrypt.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import top.lrshuai.encrypt.constant.Result;
import top.lrshuai.encrypt.dto.TestDto;

/**
 * @author rstyro
 */
@RestController
public class TestController {

    @GetMapping("/test1")
    public Object test1(@RequestBody(required = false) TestDto dto){
        System.out.println("dto="+dto);
        return Result.ok();
    }

    @GetMapping("/test2")
    public Object test2(@RequestBody(required = false)String dto){
        System.out.println("dto="+dto);
        return Result.ok();
    }

    @GetMapping("/test3")
    public Object test3(String userId){
        System.out.println("userId="+userId);
        return Result.ok();
    }

}

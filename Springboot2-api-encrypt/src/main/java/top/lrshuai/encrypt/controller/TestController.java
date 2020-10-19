package top.lrshuai.encrypt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import top.lrshuai.encrypt.constant.Result;
import top.lrshuai.encrypt.dto.TestDto;

/**
 * @author rstyro
 */
@Controller
public class TestController {

    @GetMapping("/test1")
    @ResponseBody
    public Object test1(@RequestBody(required = false) TestDto dto){
        System.out.println("dto="+dto);
        return Result.ok();
    }

    @GetMapping("/test2")
    @ResponseBody
    public Object test2(@RequestBody(required = false)String dto){
        System.out.println("dto="+dto);
        return Result.ok();
    }

    @GetMapping("/test3")
    @ResponseBody
    public Object test3(String userId){
        System.out.println("userId="+userId);
        return Result.ok();
    }


    @GetMapping("/testPage1")
    public String testPage1(){

        return "index";
    }


}

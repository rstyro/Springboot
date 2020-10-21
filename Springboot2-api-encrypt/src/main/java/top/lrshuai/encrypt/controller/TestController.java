package top.lrshuai.encrypt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import top.lrshuai.encrypt.annotation.Decode;
import top.lrshuai.encrypt.annotation.Encrypt;
import top.lrshuai.encrypt.constant.Result;
import top.lrshuai.encrypt.dto.TestDto;

/**
 * @author rstyro
 */
@CrossOrigin(origins = "*")
@Controller
public class TestController {


    @PostMapping("/test1")
    @ResponseBody
    @Encrypt
    public Object test1(@RequestBody(required = false) TestDto dto){
        System.out.println("dto="+dto);
        return Result.ok(dto);
    }

    @GetMapping("/test2")
    @ResponseBody
    public Object test2(@RequestBody(required = false) TestDto dto){
        System.out.println("dto="+dto);
        return Result.ok(dto);
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

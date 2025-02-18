package top.lrshuai.validator.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import top.lrshuai.validator.dto.TestDto;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

@Validated
@RestController
public class TestController {

    @GetMapping("/get")
    public String getUser(@NotNull(message = "name 不能为空") String name, @Max(value = 100, message = "age不能大于100岁") Integer age) {
        return "name: " + name + " ,age:" + age;
    }

    @PostMapping("/test")
    public Object test(@RequestBody @Valid TestDto dto){
        System.out.println("dto="+dto.toString());
        return "ok";
    }
}

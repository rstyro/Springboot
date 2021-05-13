package top.lrshuai.validator.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import top.lrshuai.validator.dto.TestDto;

import javax.validation.Valid;

@Validated
@RestController
public class TestController {

    @PostMapping("/test")
    public Object test(@RequestBody @Valid TestDto dto){
        System.out.println("dto="+dto.toString());
        return "ok";
    }
}

package top.lrshuai.keycloak.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/page")
@Controller
public class PageController {

    @GetMapping("/callback")
    public Object callback(){
        return "callback";
    }

    @GetMapping("/test")
    public Object test(){
        return "test";
    }
}

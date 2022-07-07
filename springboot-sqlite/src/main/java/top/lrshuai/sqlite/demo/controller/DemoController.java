package top.lrshuai.sqlite.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.lrshuai.sqlite.demo.entity.User;
import top.lrshuai.sqlite.demo.service.IUserService;

@RestController
@RequestMapping("/demo")
public class DemoController {

    private IUserService userService;

    @Autowired
    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/save")
    public Object save(Long id){
        userService.save(new User().setId(id).setAge(10).setName("rstyro").setCreateTime(System.currentTimeMillis()+""));
        System.out.println("save");
        return "success";
    }

    @GetMapping("/get")
    public Object get(Long id){
        User user = userService.getById(id);
        System.out.println("user="+user);
        System.out.println("get");
        return user;
    }
}

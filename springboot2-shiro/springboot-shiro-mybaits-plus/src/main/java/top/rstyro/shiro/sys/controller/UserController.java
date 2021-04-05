package top.rstyro.shiro.sys.controller;


import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author rstyro
 * @since 2021-04-03
 */
@Controller
@RequestMapping("/user")
public class UserController {


    @RequestMapping("/list")
    @RequiresPermissions("user:list")
    public String list(){
        System.out.println("user/list....");
        return "user/list";
    }

    @RequestMapping("/add")
    @RequiresPermissions("user:add")
    public String add(){
        return "user/add";
    }

    @RequestMapping("/edit")
    @RequiresPermissions("user:edit")
    public String edit(){
        return "user/edit";
    }

    @RequestMapping("/del")
    @RequiresPermissions("user:del")
    public String del(){
        return "user/del";
    }


}

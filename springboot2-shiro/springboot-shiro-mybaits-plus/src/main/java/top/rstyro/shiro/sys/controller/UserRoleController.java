package top.rstyro.shiro.sys.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import top.rstyro.shiro.sys.service.IUserRoleService;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author rstyro
 * @since 2021-04-03
 */
@RestController
@RequestMapping("/sys/user-role")
public class UserRoleController {

    @Autowired
    private IUserRoleService userRoleService;

    @GetMapping("/test")
    public Object testTransactional(){
        userRoleService.testTransactional();
        return "ok";
    }
}

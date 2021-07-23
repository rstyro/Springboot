package top.lrshuai.security.user.controller;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import top.lrshuai.security.commons.R;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author rstyro
 * @since 2021-07-16
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('user_list')")
    public Object list(){

        return R.ok("user-list");
    }

    @GetMapping("/list/add")
    @PreAuthorize("hasAuthority('user_list_add')")
    public Object add(){

        return R.ok("user-list-add");
    }

    @GetMapping("/list/edit")
    @PreAuthorize("hasAuthority('user_list_edit')")
    public Object edit(){

        return R.ok("user-list-edit");
    }

    @GetMapping("/list/del")
    @PreAuthorize("hasAuthority('user_list_del')")
    public Object del(){

        return R.ok("user-list-del");
    }

}

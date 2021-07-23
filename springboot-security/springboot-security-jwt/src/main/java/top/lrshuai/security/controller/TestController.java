package top.lrshuai.security.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import top.lrshuai.security.commons.R;
import top.lrshuai.security.dto.LoginDto;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/list")
    public R list(){
        return R.ok("list");
    }

    @GetMapping("/list2")
    @PreAuthorize("hasAuthority('ROLE_test')")
    public R list2(){

        return R.ok("list2");
    }

    @GetMapping("/test")
    @PreAuthorize("hasRole('test')")
    public R authTest() {
        return R.ok("需要test权限");
    }

    @GetMapping("/list3")
    @Secured("ROLE_test")
    public R list3(){

        return R.ok("list5");
    }

    @GetMapping("/user")
    @Secured("ROLE_USER")
    public R user() {
        return R.ok("需要USER权限");
    }

    @GetMapping("/both")
    @RolesAllowed({"USER","test"})
    public R both() {
        return R.ok("需要权限");
    }

    @GetMapping("/perm")
    @PermitAll
    public R perm() {
        return R.ok("允许");
    }

    @GetMapping("/deny")
    @DenyAll
    public R testDeny() {
        return R.ok("拒绝");
    }

    @PostMapping("/el1")
    @PreAuthorize("#sysUser.username == 'admin' ")
    public R elTest1(@RequestBody LoginDto sysUser) {
        return R.ok("EL测试");
    }

    @PostMapping("/el2")
    @PreAuthorize("#u.username == authentication.principal ")
    public R elTest2(@P("u") @RequestBody LoginDto sysUser) {
        return R.ok("EL测试");
    }
}

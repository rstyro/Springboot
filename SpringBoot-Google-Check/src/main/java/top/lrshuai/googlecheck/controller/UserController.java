package top.lrshuai.googlecheck.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.lrshuai.googlecheck.annotation.NeedLogin;
import top.lrshuai.googlecheck.base.BaseController;
import top.lrshuai.googlecheck.common.Result;
import top.lrshuai.googlecheck.dto.GoogleDTO;
import top.lrshuai.googlecheck.dto.LoginDTO;
import top.lrshuai.googlecheck.service.UserService;

@RestController
@RequestMapping("/user")
@Api(tags = "用户模块")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    @ApiOperation("注册")
    public Result register(LoginDTO dto) throws Exception {
        return userService.register(dto);
    }


    @GetMapping("/login")
    @ApiOperation("登录")
    public Result login(LoginDTO dto)throws Exception{
        return userService.login(dto);
    }

    @GetMapping("/getData")
    @NeedLogin(google = true)
    @ApiOperation("获取数据")
    public Result getData()throws Exception{
        return userService.getData();
    }


    @GetMapping("/generateGoogleSecret")
    @NeedLogin
    @ApiOperation("生成google密钥")
    public Result generateGoogleSecret()throws Exception{
        return userService.generateGoogleSecret(this.getUser());
    }


    @GetMapping("/bindGoogle")
    @NeedLogin
    @ApiOperation("绑定google验证")
    public Result bindGoogle(GoogleDTO dto)throws Exception{
        return userService.bindGoogle(dto,this.getUser(),this.getRequest());
    }


    @GetMapping("/googleLogin")
    @NeedLogin
    @ApiOperation("google登录")
    public Result googleLogin(Long code) throws Exception{
        return userService.googleLogin(code,this.getUser(),this.getRequest());
    }


}

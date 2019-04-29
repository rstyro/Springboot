package top.lrshuai.googlecheck.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import top.lrshuai.googlecheck.annotation.NeedLogin;
import top.lrshuai.googlecheck.base.BaseController;
import top.lrshuai.googlecheck.common.Result;
import top.lrshuai.googlecheck.dto.GoogleDTO;
import top.lrshuai.googlecheck.dto.LoginDTO;
import top.lrshuai.googlecheck.service.UserService;
import top.lrshuai.googlecheck.utils.QRCodeUtil;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;

@Controller
@RequestMapping("/user")
@Api(tags = "用户模块")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    @ApiOperation("1、注册")
    @ResponseBody
    public Result register(LoginDTO dto) throws Exception {
        return userService.register(dto);
    }


    @GetMapping("/login")
    @ApiOperation("2、登录")
    @ResponseBody
    public Result login(LoginDTO dto)throws Exception{
        return userService.login(dto);
    }


    @GetMapping("/generateGoogleSecret")
    @ResponseBody
    @NeedLogin
    @ApiOperation("3、生成google密钥")
    public Result generateGoogleSecret()throws Exception{
        return userService.generateGoogleSecret(this.getUser());
    }

    /**
     * 注意：这个需要地址栏请求,因为返回的是一个流
     * 注意：这个需要地址栏请求,因为返回的是一个流
     * 注意：这个需要地址栏请求,因为返回的是一个流
     * 显示一个二维码图片
     * @param secretQrCode   generateGoogleSecret接口返回的：secretQrCode
     * @param response
     * @throws Exception
     */
    @GetMapping("/genQrCode")
    @ApiOperation("4、生成二维码，这个去地址栏请求，不要用Swagger-ui请求")
    public void genQrCode(String secretQrCode, HttpServletResponse response) throws Exception{
        response.setContentType("image/png");
        OutputStream stream = response.getOutputStream();
        QRCodeUtil.encode(secretQrCode,stream);
    }


    @GetMapping("/bindGoogle")
    @ResponseBody
    @NeedLogin
    @ApiOperation("5、绑定google验证")
    public Result bindGoogle(GoogleDTO dto)throws Exception{
        return userService.bindGoogle(dto,this.getUser(),this.getRequest());
    }

    @GetMapping("/googleLogin")
    @ResponseBody
    @NeedLogin
    @ApiOperation("6、google登录")
    public Result googleLogin(Long code) throws Exception{
        return userService.googleLogin(code,this.getUser(),this.getRequest());
    }


    @GetMapping("/getData")
    @NeedLogin(google = true)
    @ApiOperation("7、获取数据")
    @ResponseBody
    public Result getData()throws Exception{
        return userService.getData();
    }

}

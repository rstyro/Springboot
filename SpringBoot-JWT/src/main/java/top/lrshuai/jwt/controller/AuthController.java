package top.lrshuai.jwt.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.lrshuai.jwt.common.Result;
import top.lrshuai.jwt.dto.TokenGenerateRequest;
import top.lrshuai.jwt.dto.TokenVerifyRequest;
import top.lrshuai.jwt.entity.User;
import top.lrshuai.jwt.service.JwtService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Api(tags = "JWT认证接口")
@Validated
public class AuthController {

    private final JwtService jwtService;

    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/token/generate")
    @ApiOperation(value = "生成JWT Token", notes = "支持HS256和RS256两种加密算法，可自定义用户信息和过期时间")
    public Result generateToken(@Valid @RequestBody TokenGenerateRequest request) throws Exception {
        return Result.ok(jwtService.generateToken(request));
    }

    @PostMapping("/token/verify")
    @ApiOperation(value = "验证JWT Token", notes = "验证Token有效性并返回解析后的用户数据")
    public Result verifyToken(@Valid @RequestBody TokenVerifyRequest request) throws Exception {
        User user = jwtService.verifyToken(request);
        return Result.ok(user);
    }
}

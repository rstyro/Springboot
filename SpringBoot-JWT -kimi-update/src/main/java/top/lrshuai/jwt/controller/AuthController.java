package top.lrshuai.jwt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.lrshuai.jwt.common.Result;
import top.lrshuai.jwt.dto.*;
import top.lrshuai.jwt.service.AuthService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "认证管理", description = "用户认证相关接口：登录、注册、登出、刷新Token等")
@Validated
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户登录接口，支持用户名密码登录")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return Result.success(authService.login(request));
    }

    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "用户注册接口，需要用户名、密码、确认密码，可选邮箱和手机号")
    public Result<String> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return Result.success("注册成功");
    }

    @PostMapping("/logout")
    @Operation(summary = "用户登出", description = "用户登出接口，需要传入Authorization头")
    public Result<String> logout(HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        Long userId = getCurrentUserId(request);
        authService.logout(userId, token);
        return Result.success("登出成功");
    }

    @PostMapping("/refresh")
    @Operation(summary = "刷新Token", description = "使用Refresh Token获取新的Access Token和Refresh Token")
    public Result<TokenResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        return Result.success(authService.refreshToken(request));
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private Long getCurrentUserId(HttpServletRequest request) {
        Object userId = request.getAttribute("userId");
        return userId != null ? Long.valueOf(userId.toString()) : null;
    }
}

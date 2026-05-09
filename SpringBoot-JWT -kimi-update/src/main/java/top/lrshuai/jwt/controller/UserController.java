package top.lrshuai.jwt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.lrshuai.jwt.common.Result;
import top.lrshuai.jwt.dto.ChangePasswordRequest;
import top.lrshuai.jwt.dto.UpdateProfileRequest;
import top.lrshuai.jwt.dto.UserProfileDTO;
import top.lrshuai.jwt.service.AuthService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/user")
@Tag(name = "用户管理", description = "用户资料管理相关接口")
@Validated
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    @Autowired
    private AuthService authService;

    @GetMapping("/profile")
    @Operation(summary = "获取当前用户信息", description = "获取当前登录用户的详细资料")
    public Result<UserProfileDTO> getProfile(HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        return Result.success(authService.getUserProfile(userId));
    }

    @PutMapping("/profile")
    @Operation(summary = "更新用户信息", description = "更新当前登录用户的资料信息")
    public Result<UserProfileDTO> updateProfile(@Valid @RequestBody UpdateProfileRequest request, 
                                                 HttpServletRequest httpRequest) {
        Long userId = getCurrentUserId(httpRequest);
        return Result.success(authService.updateUserProfile(userId, request));
    }

    @PutMapping("/password")
    @Operation(summary = "修改密码", description = "修改当前登录用户的密码")
    public Result<String> changePassword(@Valid @RequestBody ChangePasswordRequest request,
                                        HttpServletRequest httpRequest) {
        Long userId = getCurrentUserId(httpRequest);
        authService.changePassword(userId, request);
        return Result.success("密码修改成功，请重新登录");
    }

    private Long getCurrentUserId(HttpServletRequest request) {
        Object userId = request.getAttribute("userId");
        return userId != null ? Long.valueOf(userId.toString()) : null;
    }
}

package top.lrshuai.jwt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.lrshuai.jwt.common.Result;
import top.lrshuai.jwt.service.AuthService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "权限管理", description = "用户权限和角色查询接口")
@Validated
@SecurityRequirement(name = "bearerAuth")
public class PermissionController {

    @Autowired
    private AuthService authService;

    @GetMapping("/permissions")
    @Operation(summary = "获取当前用户权限", description = "获取当前登录用户的所有权限列表")
    public Result<Map<String, Object>> getPermissions(HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        List<String> permissions = authService.getUserPermissions(userId);
        Map<String, Object> result = new HashMap<>();
        result.put("permissions", permissions);
        result.put("count", permissions.size());
        return Result.success(result);
    }

    @GetMapping("/roles")
    @Operation(summary = "获取当前用户角色", description = "获取当前登录用户的所有角色列表")
    public Result<Map<String, Object>> getRoles(HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        List<String> roles = authService.getUserRoles(userId);
        Map<String, Object> result = new HashMap<>();
        result.put("roles", roles);
        result.put("count", roles.size());
        return Result.success(result);
    }

    private Long getCurrentUserId(HttpServletRequest request) {
        Object userId = request.getAttribute("userId");
        return userId != null ? Long.valueOf(userId.toString()) : null;
    }
}

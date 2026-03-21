package top.lrshuai.jwt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.lrshuai.jwt.common.Result;
import top.lrshuai.jwt.dto.TokenParseResponse;
import top.lrshuai.jwt.dto.TokenVerifyRequest;
import top.lrshuai.jwt.util.JwtUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/v1/token")
@Tag(name = "Token管理", description = "Token验证、解析、吊销等管理接口")
@Validated
public class TokenController {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @PostMapping("/verify")
    @Operation(summary = "验证Token", description = "验证JWT Token的有效性，返回验证结果")
    public Result<Map<String, Object>> verifyToken(@Valid @RequestBody TokenVerifyRequest request) {
        boolean valid = jwtUtils.validateToken(request.getToken());
        Map<String, Object> result = new HashMap<>();
        result.put("valid", valid);
        result.put("message", valid ? "Token有效" : "Token无效或已过期");
        return Result.success(result);
    }

    @PostMapping("/parse")
    @Operation(summary = "解析Token", description = "解析JWT Token，返回Token中包含的详细信息和声明")
    public Result<TokenParseResponse> parseToken(@Valid @RequestBody TokenVerifyRequest request) {
        TokenParseResponse response = jwtUtils.parseTokenDetails(request.getToken());
        return Result.success(response);
    }

    @PostMapping("/revoke")
    @Operation(summary = "吊销Token", description = "将Token加入黑名单，使其失效")
    public Result<String> revokeToken(HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        if (token != null) {
            redisTemplate.opsForSet().add("token:blacklist", token);
            redisTemplate.expire("token:blacklist", 7, TimeUnit.DAYS);
        }
        return Result.success("Token已吊销");
    }

    @GetMapping("/info")
    @Operation(summary = "获取当前Token信息", description = "获取当前请求中Token的详细信息")
    public Result<TokenParseResponse> getTokenInfo(HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        if (token == null) {
            return Result.error(401, "未提供Token");
        }
        TokenParseResponse response = jwtUtils.parseTokenDetails(token);
        return Result.success(response);
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}

package top.lrshuai.jwt.common;

import lombok.Getter;

@Getter
public enum ResultCode {

    SUCCESS(200, "操作成功"),
    ERROR(500, "系统错误"),
    
    // 认证相关 1000-1099
    UNAUTHORIZED(401, "未授权，请先登录"),
    FORBIDDEN(403, "无权限访问该资源"),
    TOKEN_EXPIRED(1001, "Token已过期"),
    TOKEN_INVALID(1002, "Token无效"),
    TOKEN_REVOKED(1003, "Token已被吊销"),
    REFRESH_TOKEN_EXPIRED(1004, "Refresh Token已过期"),
    
    // 用户相关 1100-1199
    USER_NOT_FOUND(1100, "用户不存在"),
    USERNAME_EXISTS(1101, "用户名已存在"),
    EMAIL_EXISTS(1102, "邮箱已被注册"),
    PHONE_EXISTS(1103, "手机号已被注册"),
    PASSWORD_ERROR(1104, "密码错误"),
    ACCOUNT_LOCKED(1105, "账号已被锁定"),
    ACCOUNT_DISABLED(1106, "账号已被禁用"),
    
    // 验证码相关 1200-1299
    CAPTCHA_ERROR(1200, "验证码错误"),
    CAPTCHA_EXPIRED(1201, "验证码已过期"),
    
    // 参数相关 1300-1399
    PARAM_ERROR(1300, "参数错误"),
    PARAM_MISSING(1301, "缺少必要参数");

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}

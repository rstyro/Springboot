package top.lrshuai.googlecheck.entity;

import lombok.Data;

/**
 * 简单的用户类
 *
 */
@Data
public class User {
    /**
     * 用户ID ，唯一标识
     */
    private String userId;
    /**
     * 用户名
     */
    private String username;
    /**
     * 登录密码
     */
    private String password;

    /**
     * google 验证的 密钥
     */
    private String googleSecret;

}

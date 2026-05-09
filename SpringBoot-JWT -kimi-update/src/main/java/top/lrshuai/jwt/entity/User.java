package top.lrshuai.jwt.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Data
public class User {

    private Long userId;
    private String username;
    private String password;
    private String nickname;
    private String email;
    private String phone;
    private String avatar;
    private Integer gender;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime lastLoginTime;
    private List<String> roles;
    private List<String> permissions;

    public static User getAdmin() {
        User user = new User();
        user.setUserId(1L);
        user.setUsername("admin");
        user.setPassword("$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5E2");
        user.setNickname("超级管理员");
        user.setEmail("admin@example.com");
        user.setPhone("13800138000");
        user.setGender(1);
        user.setStatus(1);
        user.setCreateTime(LocalDateTime.now());
        user.setLastLoginTime(LocalDateTime.now());
        user.setRoles(Arrays.asList("ROLE_ADMIN", "ROLE_USER"));
        user.setPermissions(Arrays.asList("*:*"));
        return user;
    }

    public static User getTestUser() {
        User user = new User();
        user.setUserId(2L);
        user.setUsername("user");
        user.setPassword("$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5E2");
        user.setNickname("测试用户");
        user.setEmail("user@example.com");
        user.setPhone("13800138001");
        user.setGender(1);
        user.setStatus(1);
        user.setCreateTime(LocalDateTime.now());
        user.setLastLoginTime(LocalDateTime.now());
        user.setRoles(Arrays.asList("ROLE_USER"));
        user.setPermissions(Arrays.asList("user:read", "user:update"));
        return user;
    }
}

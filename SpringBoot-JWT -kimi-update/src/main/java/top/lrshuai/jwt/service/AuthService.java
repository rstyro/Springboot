package top.lrshuai.jwt.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import top.lrshuai.jwt.common.ResultCode;
import top.lrshuai.jwt.dto.*;
import top.lrshuai.jwt.entity.User;
import top.lrshuai.jwt.exception.BusinessException;
import top.lrshuai.jwt.util.JwtUtils;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
public class AuthService {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private final Map<String, User> userStore = new ConcurrentHashMap<>();
    private final AtomicLong userIdGenerator = new AtomicLong(2);

    @PostConstruct
    public void init() {
        User admin = User.getAdmin();
        User testUser = User.getTestUser();
        userStore.put(admin.getUsername(), admin);
        userStore.put(testUser.getUsername(), testUser);
    }

    public LoginResponse login(LoginRequest request) {
        User user = userStore.get(request.getUsername());
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        if (user.getStatus() == 0) {
            throw new BusinessException(ResultCode.ACCOUNT_DISABLED);
        }

        if (!request.getPassword().equals(user.getPassword()) && 
            !verifyPassword(request.getPassword(), user.getPassword())) {
            throw new BusinessException(ResultCode.PASSWORD_ERROR);
        }

        user.setLastLoginTime(LocalDateTime.now());

        TokenResponse tokenResponse = jwtUtils.generateTokenPair(user);

        String tokenKey = "token:" + user.getUserId();
        redisTemplate.opsForValue().set(tokenKey + ":access", tokenResponse.getAccessToken(), 
                tokenResponse.getExpiresIn(), TimeUnit.SECONDS);
        redisTemplate.opsForValue().set(tokenKey + ":refresh", tokenResponse.getRefreshToken(), 
                7, TimeUnit.DAYS);

        UserProfileDTO userProfile = convertToProfileDTO(user);

        return LoginResponse.builder()
                .user(userProfile)
                .token(tokenResponse)
                .build();
    }

    public void register(RegisterRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "两次输入的密码不一致");
        }

        if (userStore.containsKey(request.getUsername())) {
            throw new BusinessException(ResultCode.USERNAME_EXISTS);
        }

        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            boolean emailExists = userStore.values().stream()
                    .anyMatch(u -> request.getEmail().equals(u.getEmail()));
            if (emailExists) {
                throw new BusinessException(ResultCode.EMAIL_EXISTS);
            }
        }

        if (request.getPhone() != null && !request.getPhone().isEmpty()) {
            boolean phoneExists = userStore.values().stream()
                    .anyMatch(u -> request.getPhone().equals(u.getPhone()));
            if (phoneExists) {
                throw new BusinessException(ResultCode.PHONE_EXISTS);
            }
        }

        User user = new User();
        user.setUserId(userIdGenerator.incrementAndGet());
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setNickname(request.getNickname() != null ? request.getNickname() : request.getUsername());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setGender(0);
        user.setStatus(1);
        user.setCreateTime(LocalDateTime.now());
        user.setRoles(Arrays.asList("ROLE_USER"));
        user.setPermissions(Arrays.asList("user:read", "user:update"));

        userStore.put(user.getUsername(), user);
        log.info("用户注册成功: {}", user.getUsername());
    }

    public void logout(Long userId, String token) {
        String tokenKey = "token:" + userId;
        redisTemplate.delete(tokenKey + ":access");
        redisTemplate.delete(tokenKey + ":refresh");
        
        if (token != null) {
            redisTemplate.opsForSet().add("token:blacklist", token);
            redisTemplate.expire("token:blacklist", 7, TimeUnit.DAYS);
        }
        
        log.info("用户登出成功: userId={}", userId);
    }

    public TokenResponse refreshToken(RefreshTokenRequest request) {
        if (!jwtUtils.validateToken(request.getRefreshToken())) {
            throw new BusinessException(ResultCode.REFRESH_TOKEN_EXPIRED);
        }

        Long userId = jwtUtils.getUserIdFromToken(request.getRefreshToken());
        User user = findUserById(userId);
        
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        String tokenKey = "token:" + userId;
        String storedRefreshToken = redisTemplate.opsForValue().get(tokenKey + ":refresh");
        
        if (storedRefreshToken == null || !storedRefreshToken.equals(request.getRefreshToken())) {
            throw new BusinessException(ResultCode.TOKEN_INVALID, "Refresh Token无效或已过期");
        }

        TokenResponse newTokenPair = jwtUtils.generateTokenPair(user);
        
        redisTemplate.opsForValue().set(tokenKey + ":access", newTokenPair.getAccessToken(), 
                newTokenPair.getExpiresIn(), TimeUnit.SECONDS);
        redisTemplate.opsForValue().set(tokenKey + ":refresh", newTokenPair.getRefreshToken(), 
                7, TimeUnit.DAYS);

        return newTokenPair;
    }

    public User findUserById(Long userId) {
        return userStore.values().stream()
                .filter(u -> u.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }

    public User findUserByUsername(String username) {
        return userStore.get(username);
    }

    public UserProfileDTO getUserProfile(Long userId) {
        User user = findUserById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        return convertToProfileDTO(user);
    }

    public UserProfileDTO updateUserProfile(Long userId, UpdateProfileRequest request) {
        User user = findUserById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        if (request.getNickname() != null) {
            user.setNickname(request.getNickname());
        }
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }
        if (request.getAvatar() != null) {
            user.setAvatar(request.getAvatar());
        }
        if (request.getGender() != null) {
            user.setGender(request.getGender());
        }

        return convertToProfileDTO(user);
    }

    public void changePassword(Long userId, ChangePasswordRequest request) {
        User user = findUserById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "两次输入的新密码不一致");
        }

        if (!request.getOldPassword().equals(user.getPassword()) && 
            !verifyPassword(request.getOldPassword(), user.getPassword())) {
            throw new BusinessException(ResultCode.PASSWORD_ERROR, "旧密码错误");
        }

        user.setPassword(request.getNewPassword());
        
        String tokenKey = "token:" + userId;
        redisTemplate.delete(tokenKey + ":access");
        redisTemplate.delete(tokenKey + ":refresh");
    }

    public List<String> getUserPermissions(Long userId) {
        User user = findUserById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        return user.getPermissions() != null ? user.getPermissions() : new ArrayList<>();
    }

    public List<String> getUserRoles(Long userId) {
        User user = findUserById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        return user.getRoles() != null ? user.getRoles() : new ArrayList<>();
    }

    private boolean verifyPassword(String inputPassword, String storedPassword) {
        return inputPassword.equals(storedPassword);
    }

    private UserProfileDTO convertToProfileDTO(User user) {
        return UserProfileDTO.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .phone(user.getPhone())
                .avatar(user.getAvatar())
                .gender(user.getGender())
                .status(user.getStatus())
                .roles(user.getRoles())
                .permissions(user.getPermissions())
                .createTime(user.getCreateTime())
                .lastLoginTime(user.getLastLoginTime())
                .build();
    }
}

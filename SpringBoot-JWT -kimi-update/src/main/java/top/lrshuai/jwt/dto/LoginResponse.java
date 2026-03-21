package top.lrshuai.jwt.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "登录响应结果")
public class LoginResponse {

    @Schema(description = "用户信息")
    private UserProfileDTO user;

    @Schema(description = "Token信息")
    private TokenResponse token;
}

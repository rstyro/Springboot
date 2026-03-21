package top.lrshuai.jwt.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Token响应结果")
public class TokenResponse {

    @Schema(description = "访问令牌(Access Token)", example = "eyJhbGciOiJIUzI1NiIs...")
    private String accessToken;

    @Schema(description = "刷新令牌(Refresh Token)", example = "eyJhbGciOiJIUzI1NiIs...")
    private String refreshToken;

    @Schema(description = "令牌类型", example = "Bearer")
    private String tokenType;

    @Schema(description = "Access Token过期时间(秒)", example = "7200")
    private Long expiresIn;

    @Schema(description = "Access Token过期时间", example = "2024-01-01T12:00:00")
    private LocalDateTime expireTime;

    @Schema(description = "Refresh Token过期时间", example = "2024-01-08T12:00:00")
    private LocalDateTime refreshExpireTime;
}

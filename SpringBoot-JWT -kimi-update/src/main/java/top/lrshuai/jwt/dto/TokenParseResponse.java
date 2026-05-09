package top.lrshuai.jwt.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Token解析响应结果")
public class TokenParseResponse {

    @Schema(description = "Token ID", example = "jwt-id-xxx")
    private String id;

    @Schema(description = "签发者", example = "jwt-system")
    private String issuer;

    @Schema(description = "主题", example = "user-auth")
    private String subject;

    @Schema(description = "受众", example = "web-app")
    private String audience;

    @Schema(description = "签发时间", example = "2024-01-01T10:00:00")
    private LocalDateTime issuedAt;

    @Schema(description = "过期时间", example = "2024-01-01T12:00:00")
    private LocalDateTime expiration;

    @Schema(description = "生效时间", example = "2024-01-01T10:00:00")
    private LocalDateTime notBefore;

    @Schema(description = "用户ID", example = "1")
    private Long userId;

    @Schema(description = "用户名", example = "admin")
    private String username;

    @Schema(description = "自定义声明")
    private Map<String, Object> claims;

    @Schema(description = "是否有效", example = "true")
    private Boolean valid;

    @Schema(description = "验证消息", example = "Token验证通过")
    private String message;
}

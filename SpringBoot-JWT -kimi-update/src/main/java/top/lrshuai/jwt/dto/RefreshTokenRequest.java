package top.lrshuai.jwt.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Schema(description = "刷新Token请求参数")
public class RefreshTokenRequest {

    @NotBlank(message = "刷新令牌不能为空")
    @Schema(description = "刷新令牌(Refresh Token)", required = true, example = "eyJhbGciOiJIUzI1NiIs...")
    private String refreshToken;
}

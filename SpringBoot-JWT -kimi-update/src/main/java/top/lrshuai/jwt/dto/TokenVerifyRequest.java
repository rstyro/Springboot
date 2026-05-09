package top.lrshuai.jwt.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Schema(description = "Token验证请求参数")
public class TokenVerifyRequest {

    @NotBlank(message = "Token不能为空")
    @Schema(description = "JWT Token", required = true, example = "eyJhbGciOiJIUzI1NiIs...")
    private String token;
}

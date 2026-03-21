package top.lrshuai.jwt.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(description = "Token验证请求参数")
public class TokenVerifyRequest {

    @ApiModelProperty(value = "JWT Token", required = true, example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9...")
    @NotBlank(message = "Token不能为空")
    private String token;

    @ApiModelProperty(value = "加密算法类型", required = true, example = "HS256", allowableValues = "HS256,RS256")
    @NotBlank(message = "加密算法类型不能为空")
    private String algorithm;
}

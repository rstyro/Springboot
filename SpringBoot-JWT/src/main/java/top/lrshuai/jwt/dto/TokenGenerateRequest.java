package top.lrshuai.jwt.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(description = "Token生成请求参数")
public class TokenGenerateRequest {

    @ApiModelProperty(value = "加密算法类型", required = true, example = "HS256", allowableValues = "HS256,RS256")
    @NotBlank(message = "加密算法类型不能为空")
    private String algorithm;

    @ApiModelProperty(value = "用户ID", example = "1")
    private Long userId;

    @ApiModelProperty(value = "用户名", example = "admin")
    private String username;

    @ApiModelProperty(value = "过期时间(秒)，不传则使用默认值7200秒", example = "7200")
    private Long expireSeconds;

    @ApiModelProperty(value = "是否生成短期token(20秒)", example = "false")
    private Boolean shortLived;
}

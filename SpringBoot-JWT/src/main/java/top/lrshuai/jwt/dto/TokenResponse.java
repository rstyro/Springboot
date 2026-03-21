package top.lrshuai.jwt.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Token响应结果")
public class TokenResponse {

    @ApiModelProperty(value = "JWT Token字符串")
    private String token;

    @ApiModelProperty(value = "加密算法类型")
    private String algorithm;

    @ApiModelProperty(value = "过期时间(时间戳)")
    private Long expireTime;

    @ApiModelProperty(value = "过期时间(格式化字符串)")
    private String expireTimeStr;
}

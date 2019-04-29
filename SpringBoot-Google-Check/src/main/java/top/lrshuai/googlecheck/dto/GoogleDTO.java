package top.lrshuai.googlecheck.dto;

import lombok.Data;

@Data
public class GoogleDTO {
    /**
     * google密钥
     */
    private String secret;
    /**
     * 手机上显示的验证码
     */
    private Long code;
}

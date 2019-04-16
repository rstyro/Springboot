package top.lrshuai.jwt.dto;

import lombok.Data;

@Data
public class JWTDTO {
    // 加密方式
    private String alg;

    // token
    private String token;
}

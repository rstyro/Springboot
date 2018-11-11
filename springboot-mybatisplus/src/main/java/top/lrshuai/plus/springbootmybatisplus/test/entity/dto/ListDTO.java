package top.lrshuai.plus.springbootmybatisplus.test.entity.dto;

import lombok.Data;

@Data
public class ListDTO {
    private int pageSize;
    private int pageNo;
    private Integer status;
}

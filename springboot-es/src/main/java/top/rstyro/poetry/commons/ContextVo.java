package top.rstyro.poetry.commons;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 上下文参数 vo
 */
@Data
@Accessors(chain = true)
public class ContextVo {
    /**
     * 请求追踪ID
     */
    private String trackerId;
    /**
     * 分页参数
     */
    private Integer pageNo=1;
    private Integer pageSize=10;

}

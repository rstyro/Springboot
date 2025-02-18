package top.rstyro.poetry.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SuggestVo {
    /**
     * 类型：author 、 title、content
     */
    private String type;
    /**
     * 内容
     */
    private String text;
}

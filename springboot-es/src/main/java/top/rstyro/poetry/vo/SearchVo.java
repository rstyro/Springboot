package top.rstyro.poetry.vo;

import lombok.Data;
import top.rstyro.poetry.es.vo.BaseEsVo;

import java.util.List;
import java.util.Set;

@Data
public class SearchVo extends BaseEsVo {
    /**
     * 作者
     */
    private String author;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private List<String> content;

    /**
     * 标签: 春天、写景、离别....等等
     */
    private Set<String> tags;

}

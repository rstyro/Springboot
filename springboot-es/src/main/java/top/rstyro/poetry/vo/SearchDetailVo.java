package top.rstyro.poetry.vo;

import lombok.Data;
import top.rstyro.poetry.es.vo.BaseEsVo;

import java.util.List;
import java.util.Set;

@Data
public class SearchDetailVo extends BaseEsVo {
    /**
     * 篇名
     */
    private String section;
    /**
     * 章节
     */
    private String chapter;
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

    /**
     * 朝代： 唐 宋 ....
     */
    private Set<String> dynasty;

    /**
     * 七言绝句、五言律诗...
     */
    private Set<String> type;

    /**
     * 译文
     */
    private List<String> translations;
}

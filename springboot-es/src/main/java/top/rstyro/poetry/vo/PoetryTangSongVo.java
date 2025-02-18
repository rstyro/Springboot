package top.rstyro.poetry.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Set;

/**
 * 唐宋
 */
@Data
@Accessors(chain = true)
public class PoetryTangSongVo {
    /**
     * 标题
     */
    private String title;
    private String id;
    /**
     * 作者
     */
    private String author;

    /**
     * 内容
     */
    private List<String> paragraphs;
    /**
     * 标签，不一定有
     */
    private Set<String> tags;

    /**
     * 卷，不一定有
     */
    private String volume;

}

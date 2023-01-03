package top.rstyro.poetry.es.index;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * 古诗索引
 */
@Data
@Accessors(chain = true)
public class PoetryIndex extends EsBaseIndex{
    /**
     * 篇名 合集
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
     * 浏览数
     */
    private Integer view_count=0;
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
     * 七言绝句、五言律诗、...
     */
    private Set<String> type;

    /**
     * 译文
     */
    private List<String> translations;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime update_time;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime create_time;

}

package top.rstyro.poetry.es.index;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;

@Data
@Accessors(chain = true)
public class PoetIndex extends EsBaseIndex{
    /**
     * 简介
     */
    private String introduce;
    /**
     * 作者名称
     */
    private String author;

    /**
     * 标签
     */
    private Set<String> tags;


}

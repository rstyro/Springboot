package top.rstyro.poetry.vo;

import lombok.Data;
import lombok.experimental.Accessors;
import top.rstyro.poetry.es.vo.BaseEsVo;

@Data
@Accessors(chain = true)
public class FlyFlowerVo extends BaseEsVo {

    private String id;
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
    private String content;

}

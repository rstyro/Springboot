package top.rstyro.poetry.es.index;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@Accessors(chain = true)
public class EsBaseIndex implements Serializable {
    /**
     * es 主键ID
     */
    @JSONField(serialize = false)
    private String _id;
    /**
     * 命中分数
     */
    @JSONField(serialize = false)
    private float _score;


    /**
     * 高亮
     */
    @JSONField(serialize = false)
    Map<String, List<String>> highlight;

    /**
     * es 索引名称
     * @return
     */
    @JSONField(serialize = false)
    public String getIndexName() {
        return StrUtil.toSymbolCase(StrUtil.removeSuffix(getClass().getSimpleName(), "Index"), '-');
    }


}

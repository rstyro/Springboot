package top.rstyro.poetry.es.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Accessors(chain = true)
@Data
public class EsSearchResultVo<T extends BaseEsVo> implements Serializable {
    /**
     * 总数量
     */
    private long total;
    /**
     * 请求耗时
     */
    private long took;
    /**
     * 数据
     */
    private List<T> records;
    /**
     * 聚合结果
     */
    private List<AggregationVo<?>> aggregation = new ArrayList<>();
    /**
     * 扩展数据
     */
    private Map<String, Object> extraParam = new HashMap<>();

    public void addAggregation(AggregationVo<?> dto) {
        aggregation.add(dto);
    }

    public void addExtraParam(String key, Object value) {
        extraParam.put(key, value);
    }
}

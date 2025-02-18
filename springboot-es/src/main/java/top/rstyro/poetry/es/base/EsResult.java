package top.rstyro.poetry.es.base;

import lombok.Data;
import lombok.experimental.Accessors;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.suggest.Suggest;
import top.rstyro.poetry.es.index.EsBaseIndex;

import java.io.Serializable;
import java.util.List;

/**
 * 通用返回
 * @param <T>
 */
@Data
@Accessors(chain = true)
public class EsResult<T extends EsBaseIndex> implements Serializable {

    /**
     * 请求时间
     */
    private long took;
    /**
     * 总数
     */
    private long total;
    /**
     * 是否超时
     */
    private boolean timed_out;
    /**
     * 数据集合
     */
    private List<T> records;

    /**
     * 聚合
     */
    Aggregations aggregation;
    /**
     * 自动补全
     */
    Suggest suggest;

}

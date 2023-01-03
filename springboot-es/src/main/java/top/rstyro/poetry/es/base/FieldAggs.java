package top.rstyro.poetry.es.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 检索返回实体类基类
 */
@Data
@Accessors(chain = true)
public class FieldAggs extends EsBaseAggs implements Serializable {
    private List<AggVo> list = new ArrayList<>();

    public void add(AggVo aggs){
        list.add(aggs);
    }

    @Data
    @AllArgsConstructor
    public class AggVo{
        private String key;
        private long docCount;
    }
}

package top.rstyro.poetry.es.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TermAggregationVo extends BaseAggregationVo {
    public Long docCount;
}

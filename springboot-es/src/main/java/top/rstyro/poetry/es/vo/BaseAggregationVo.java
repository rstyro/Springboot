package top.rstyro.poetry.es.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class BaseAggregationVo implements Serializable {
    public String key;
}

package top.rstyro.poetry.es.base;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 聚合基类
 */
@Data
@Accessors(chain = true)
public class EsBaseAggs implements Serializable {
    private String key;
}

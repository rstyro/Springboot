package top.rstyro.poetry.es.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 搜索基础VO
 */
@Data
@Accessors(chain = true)
public class BaseEsVo implements Serializable {
    public String _id;
    public Map<String, List<String>> highlight;
}

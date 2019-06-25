package top.lrshuai.redisson.subscribe;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class MyObjectDTO implements Serializable {
    private Long id;
    private String content;
}

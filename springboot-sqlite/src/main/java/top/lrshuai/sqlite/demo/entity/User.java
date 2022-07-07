package top.lrshuai.sqlite.demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
@TableName("demo")
public class User {
    private Long id;
    private String name;
    private Integer age;
    private String createTime;
}

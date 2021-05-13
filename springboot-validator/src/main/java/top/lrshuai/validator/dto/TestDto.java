package top.lrshuai.validator.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import top.lrshuai.validator.annotation.MatchValue;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
@Accessors(chain = true)
public class TestDto {

    @NotBlank(message = "name不能为空")
    private String name;

    @MatchValue(values = {"男","女"},message = "sex参数无效")
    private String sex;

    @NotNull(message = "age 不能为空")
    @Min(value = 1,message = "age最小是1")
    @Max(value = 200,message = "age最大为200")
    private Integer age;
}

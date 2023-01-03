package top.rstyro.poetry.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SearchAggsDto {
    private String key;
    private Integer size=10;
}

package top.lrshuai.playwright.script;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ScriptContent {

    private String content;        // groovy 脚本源码
    private long lastModified;     // 最后修改时间（文件时间戳或数据库更新时间）
    private SourceType sourceType; // FILE, DB

    // 构造器 + getter
    public boolean isNewerThan(ScriptContent other) {
        return this.lastModified > other.lastModified;
    }
}

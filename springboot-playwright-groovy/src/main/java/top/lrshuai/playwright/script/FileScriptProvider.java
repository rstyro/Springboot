package top.lrshuai.playwright.script;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 文件脚本
 */
@Slf4j
@Component
public class FileScriptProvider implements ScriptProvider {
    private final String scriptBasePath;

    private final ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();

    public FileScriptProvider(@Value("${script.file.path:classpath:scripts/}") String path) {
        this.scriptBasePath = path;
    }

    @Override
    public ScriptContent getScript(String scriptName) {
        // 只处理不以 "db:" 开头的名字
        if (!supports(scriptName)) return null;
        String location = scriptBasePath + scriptName + ".groovy";
        System.out.println("location=="+location);
        Resource resource = resourceResolver.getResource(location);
        if (!resource.exists()) {
            log.warn("文件脚本不存在: {}", location);
            return null;
        }
        try {
            String content = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
            long lastModified = resource.getFile().lastModified();
            return new ScriptContent(content, lastModified, SourceType.FILE);
        } catch (IOException e) {
            throw new RuntimeException("读取文件脚本失败", e);
        }
    }

    @Override
    public boolean supports(String scriptName) {
        return !scriptName.startsWith("db:");
    }
}

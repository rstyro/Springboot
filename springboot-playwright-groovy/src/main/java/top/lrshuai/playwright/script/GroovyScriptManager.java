package top.lrshuai.playwright.script;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyRuntimeException;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class GroovyScriptManager {

    private final GroovyClassLoader classLoader = new GroovyClassLoader();
    private final Map<String, Class<?>> scriptCache = new ConcurrentHashMap<>();
    private final Map<String, ScriptContent> contentCache = new ConcurrentHashMap<>();
    private final List<ScriptProvider> providers;

    public GroovyScriptManager(List<ScriptProvider> providers) {
        this.providers = providers;
    }

    /**
     * 执行脚本中的指定方法
     * @param scriptName 脚本名，支持 "login" 或 "db:login"
     * @param methodName 方法名
     * @param args 参数
     * @return 方法返回值
     */
    public Object execute(String scriptName, String methodName, Object... args) {
        Class<?> clazz = getScriptClass(scriptName);
        Object instance;
        try {
            instance = clazz.getDeclaredConstructor().newInstance();
            Method method = findMethod(clazz, methodName, args);
            return method.invoke(instance, args);
        } catch (Exception e) {
            throw new RuntimeException("执行脚本失败: " + scriptName, e);
        }
    }

    private Class<?> getScriptClass(String scriptName) {
        ScriptContent latest = fetchLatestContent(scriptName);
        ScriptContent cached = contentCache.get(scriptName);
        if (cached == null || !cached.isNewerThan(latest)) {
            // 有更新，重新编译
            Class<?> clazz = compile(scriptName, latest.getContent());
            scriptCache.put(scriptName, clazz);
            contentCache.put(scriptName, latest);
        }
        return scriptCache.get(scriptName);
    }

    private ScriptContent fetchLatestContent(String scriptName) {
        for (ScriptProvider provider : providers) {
            if (provider.supports(scriptName)) {
                ScriptContent content = provider.getScript(scriptName);
                if (content != null) return content;
            }
        }
        throw new IllegalArgumentException("未找到脚本: " + scriptName);
    }

    private Class<?> compile(String scriptName, String content) {
        try {
            return classLoader.parseClass(content, scriptName);
        } catch (GroovyRuntimeException e) {
            throw new RuntimeException("Groovy 编译错误: " + scriptName + " - " + e.getMessage(), e);
        }
    }

    private Method findMethod(Class<?> clazz, String methodName, Object[] args) throws NoSuchMethodException {
        for (Method m : clazz.getMethods()) {
            if (m.getName().equals(methodName) && m.getParameterCount() == args.length) {
                return m;
            }
        }
        throw new NoSuchMethodException(methodName);
    }

    /**
     * 手动刷新指定脚本（如数据库主动更新后调用）
     */
    public void refresh(String scriptName) {
        scriptCache.remove(scriptName);
        contentCache.remove(scriptName);
    }
}
# Spring Boot集成 Playwright + Groovy 执行动态测试脚本

## 一、项目概述

本项目是一个基于 **Spring Boot 3.5**、**Playwright** 和 **Groovy** 的浏览器自动化测试框架。项目实现了动态脚本执行机制，支持通过 Groovy 脚本灵活定义和执行浏览器自动化测试流程。

### 1、Playwright是什么，有什么用

**Playwright** 是由 Microsoft 开发的一个强大的浏览器自动化测试工具，支持 Chromium、Firefox 和 WebKit 三大浏览器引擎。

**核心特性：**
- **跨浏览器支持**：统一 API 操作 Chrome、Firefox、Safari 等主流浏览器
- **无头模式**：支持无界面运行，适合 CI/CD 环境
- **自动等待**：智能等待元素就绪，无需手动添加 sleep
- **多语言支持**：JavaScript/TypeScript、Python、C#、Java
- **强大选择器**：支持 CSS、XPath、文本选择器等多种定位方式
- **网络拦截**：可以拦截、修改网络请求和响应
- **模拟用户行为**：支持鼠标、键盘、触摸等操作

**主要用途：**
- **Web 自动化测试**：自动化执行功能测试、回归测试
- **端到端测试**：模拟真实用户场景进行测试
- **网页爬虫**：抓取动态渲染的网页内容
- **性能测试**：分析页面加载性能
- **浏览器自动化**：自动完成重复性任务

### 2、Groovy是什么，有什么用

**Groovy** 是一种基于 JVM 的动态编程语言，兼具 Python、Ruby 和 Smalltalk 的特性，同时保持与 Java 的完全兼容。

**核心特性：**
- **语法简洁**：比 Java 更简洁的语法，支持闭包、动态类型
- **完全兼容 Java**：可以直接调用 Java 类和库
- **脚本语言特性**：支持脚本式编程，无需编译即可运行
- **元编程能力**：支持运行时修改类行为
- **DSL 支持**：易于构建领域特定语言

**主要用途：**
- **脚本编写**：快速编写自动化脚本和工具
- **测试框架**：常用作测试脚本语言（如 Spock 测试框架）
- **动态配置**：运行时动态加载和执行代码
- **构建工具**：Gradle 构建工具使用 Groovy 作为脚本语言
- **快速原型开发**：快速验证想法和原型

**在本项目中的应用：**
- 浏览器自动化测试脚本可以动态编写和修改
- 无需重新编译 Java 代码即可更新测试逻辑
- 支持热更新，脚本修改后立即生效


### 3、Playwright + Groovy 解决了什么痛点

**传统 Playwright 测试的痛点：**
- **代码固化**：测试逻辑硬编码在 Java 代码中，每次新增测试场景都需要修改代码并重新部署
- **代码膨胀**：多个需求的测试代码堆积导致项目体积庞大，维护困难
- **灵活性差**：测试脚本无法动态调整，难以快速响应业务变化

**Playwright + Groovy 的解决方案：**
- **脚本解耦**：将测试逻辑抽取为独立的 Groovy 脚本，与主应用代码分离
- **动态加载**：测试脚本可从文件、数据库等多种来源动态加载，无需重启服务
- **热更新支持**：脚本修改后立即生效，无需重新编译部署
- **扩展性强**：新增测试场景只需编写新脚本，不影响现有代码结构

## 二、快速开始

### 2.1 技术栈

- **JDK 版本**: JDK17+
- **Spring Boot**: 3.5.14
- **Playwright**: 1.59.0
- **Groovy**: 4.0.31

### 2.2 项目依赖

```xml
<properties>
    <java.version>17</java.version>
    <playwright.version>1.59.0</playwright.version>
    <groovy.version>4.0.31</groovy.version>
</properties>
<dependencies>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
</dependency>

<!-- playwright 自动化测试 -->
<dependency>
    <groupId>com.microsoft.playwright</groupId>
    <artifactId>playwright</artifactId>
    <version>${playwright.version}</version>
</dependency>

<!-- groovy动态脚本 -->
<dependency>
    <groupId>org.apache.groovy</groupId>
    <artifactId>groovy-all</artifactId>
    <version>${groovy.version}</version>
    <type>pom</type>
</dependency>

</dependencies>

```

### 2.3 脚本管理核心模块


#### 2.3.1 ScriptProvider脚本提供者接口


```java

/**
 * 自定义的脚本实体类
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ScriptContent {

    private String content;        // groovy 脚本源码
    private long lastModified;     // 最后修改时间（文件时间戳或数据库更新时间）
    private SourceType sourceType; // FILE, DB

    // 判断是不是更新的文件
    public boolean isNewerThan(ScriptContent other) {
        return this.lastModified > other.lastModified;
    }
}

/**
 * 脚本提供者接口,扩展多种脚本
 */
public interface ScriptProvider {
    ScriptContent getScript(String scriptName);
    boolean supports(String scriptName);
    default void refresh(String scriptName) {}
}
```

- 目前只实现了`FileScriptProvider`文件提供者，可以保留一下默认的脚本文件
- 可以实现这个接口扩展多种脚本来源，比如从数据库查询等等

#### 2.3.2 扩展文件来源脚本FileScriptProvider

```java
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
```

文件脚本提供者，从 `classpath:scripts/` 目录加载 `.groovy` 脚本文件。


#### 2.3.3 脚本管理器 GroovyScriptManager

```java
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
```

- 负责脚本的加载、编译、缓存和执行。

**核心功能：**
- 支持多脚本源（文件、数据库等）
- 脚本内容自动检测更新，无需重启
- 基于 `ConcurrentHashMap` 的线程安全缓存


**关键方法：**

| 方法 | 功能 |
| :--- | :--- |
| `execute(scriptName, methodName, args)` | 执行指定脚本的指定方法 |
| `refresh(scriptName)` | 手动刷新指定脚本缓存 |



### 2.4 实战示例

#### 脚本规范

1. 脚本文件放置在 `src/main/resources/scripts/` 目录
2. 文件名即为脚本名（不含 `.groovy` 后缀）
3. 脚本类名需与文件名一致

#### 示例脚本：TestLoginScript.groovy

```groovy
class TestLoginScript {
    
    private static final String LOGIN_URL = "http://example.com/login"
    
    boolean testLogin(String username, String password) {
        try (Playwright playwright = Playwright.create();
             Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                     .setHeadless(false)
                     .setSlowMo(50))) {
            
            Page page = browser.newPage()
            page.navigate(LOGIN_URL)
            
            page.fill("input[placeholder='用户名']", username)
            page.fill("input[placeholder='密码']", password)
            page.click(".login-button")
            // todo ....
            
            return true;
        }
    }
}
```

#### 执行脚本示例

```java
@Slf4j
@Service
@RequiredArgsConstructor
public class TestServiceImpl implements ITestService {

    private final GroovyScriptManager scriptManager;
    
    // 验证登录的脚步文件名称
    private static final String LOGIN_SCRIPT_NAME = "TestLoginScript";

    public boolean testGroovyLogin(TestLoginDto dto) {
        try {
            log.info("开始执行 Groovy 登录脚本，用户名: {}, 脚本: {}", dto.getUsername(), LOGIN_SCRIPT_NAME);
            Object result = scriptManager.execute(LOGIN_SCRIPT_NAME, "testLogin", dto.getUsername(), dto.getPassword());
            boolean success = (Boolean) result;
            log.info("Groovy 脚本执行完成，结果: {}", success);
            return success;
        } catch (ServiceException e) {
            log.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("执行 Groovy 登录脚本失败", e);
            throw new ServiceException("执行 Groovy 登录脚本失败");
        }
    }
}
```



-  **注意**：首次运行时 Playwright 会自动下载浏览器驱动




## 三、扩展说明

### 添加新的脚本来源

实现 `ScriptProvider` 接口即可扩展新的脚本来源，例如从数据库加载脚本：

```java
@Component
public class DatabaseScriptProvider implements ScriptProvider {
    
    @Override
    public ScriptContent getScript(String scriptName) {
        if (!supports(scriptName)) return null;
        // 从数据库查询脚本内容
        String content = scriptRepository.findByKey(scriptName.substring(3));
        return new ScriptContent(content, System.currentTimeMillis(), SourceType.DB);
    }
    
    @Override
    public boolean supports(String scriptName) {
        return scriptName.startsWith("db:");
    }
}
```

使用时脚本名前缀为 `db:`，如 `db:loginScript`。






## 四、总结

本项目成功实现了 **Spring Boot + Playwright + Groovy** 的技术整合，构建了一个灵活、高效的浏览器自动化测试框架。

### 核心价值

1. **动态脚本执行**：通过 Groovy 脚本实现测试逻辑的动态加载和执行，无需修改 Java 代码即可扩展测试场景。
2. **自动化测试能力**：基于 Playwright 实现跨浏览器自动化测试，支持无头模式运行，适合 CI/CD 集成。


### 技术亮点

- **脚本热更新**：脚本内容变更后自动检测并重新编译，无需重启服务。
- **多脚本源支持**：通过 `ScriptProvider` 接口可扩展多种脚本来源（文件、数据库等）。
- **线程安全设计**：使用 `ConcurrentHashMap` 保证脚本缓存的线程安全性。
- **优雅的异常处理**：统一的异常处理机制，提供清晰的错误信息。


### 适用场景

- **Web 自动化测试**：适用于需要频繁更新测试用例的场景。
- **端到端测试**：模拟真实用户操作流程进行完整业务链路测试。
- **自动化爬虫**：动态加载爬虫脚本，灵活应对目标网站变化。
- **回归测试**：快速执行回归测试用例，确保代码变更不影响现有功能。


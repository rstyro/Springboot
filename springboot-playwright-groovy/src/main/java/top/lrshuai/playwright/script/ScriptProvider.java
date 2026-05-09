package top.lrshuai.playwright.script;

public interface ScriptProvider {
    /**
     * @param scriptName 脚本标识（如 "login" 或 "db:login"）
     * @return 脚本内容，若不存在返回 null
     */
    ScriptContent getScript(String scriptName);

    /**
     * 判断该 provider 是否支持此 scriptName
     */
    boolean supports(String scriptName);

    /**
     * 触发刷新（例如数据库主动通知）
     */
    default void refresh(String scriptName) {}
}

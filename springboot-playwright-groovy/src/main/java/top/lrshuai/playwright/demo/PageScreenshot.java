package top.lrshuai.playwright.demo;


import com.microsoft.playwright.*;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class PageScreenshot {
    public static void main(String[] args) {

        // 使用 try-with-resources 确保 Playwright 资源被正确关闭
        try (Playwright playwright = Playwright.create()) {
            // 启动 Chromium 浏览器
            Browser browser = playwright.chromium().launch();
            // 创建一个新页面
            Page page = browser.newPage();
            // 导航到目标网站
            page.navigate("https://playwright.dev");
            // 打印页面标题
            System.out.println(page.title());
        }

        System.out.println("===============================");
        try (Playwright playwright = Playwright.create()) {
            // playwright 分别获取三种浏览器类型
            List<BrowserType> browserTypes = Arrays.asList(
                    playwright.chromium(),
                    playwright.webkit(),
                    playwright.firefox()
            );
            for (BrowserType browserType : browserTypes) {
                try (Browser browser = browserType.launch()) {
                    BrowserContext context = browser.newContext();
                    Page page = context.newPage();
                    page.navigate("https://playwright.dev/");
                    // browserType.name() 返回浏览器类型的名字（如 chromium、webkit、firefox）
                    // 保存快照图片到当前目录
                    page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("screenshot-" + browserType.name() + ".png")));
                }
            }
        }
    }
}

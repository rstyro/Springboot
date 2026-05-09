package top.lrshuai.playwright.service.impl;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.PageAssertions;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.BoundingBox;
import com.microsoft.playwright.options.FilePayload;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.lrshuai.playwright.controller.dto.TestLoginDto;
import top.lrshuai.playwright.exception.ServiceException;
import top.lrshuai.playwright.script.GroovyScriptManager;
import top.lrshuai.playwright.service.ITestService;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;


@Slf4j
@Service
@RequiredArgsConstructor
public class TestServiceImpl implements ITestService {

    private final GroovyScriptManager scriptManager;

    private static final String LOGIN_SCRIPT_NAME = "TestLoginScript";

    @Override
    public Object example() {
        Map<String, Object> result = new HashMap<>();

        try (Playwright playwright = Playwright.create();
             Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                     .setHeadless(false)                    // 有头模式，便于观察
                     .setSlowMo(100)                        // 放慢操作，看得清楚
                     .setTimeout(60000)                     // 启动超时60秒
                     .setArgs(Arrays.asList("--start-maximized")) // 最大化窗口
                     .setChannel("chrome"));
             Page page = browser.newPage()) {

            log.info("===== 必应搜索示例 =====");

            // 1. 导航到必应首页
            page.navigate("https://www.bing.com");
            page.waitForLoadState(LoadState.NETWORKIDLE);
            log.info("1. 已打开必应首页");

            // 2. 定位搜索输入框（必应搜索框 id="sb_form_q" 或 name="q"）
            Locator searchBox = page.locator("#sb_form_q");
            searchBox.fill("Playwright Java 自动化测试");
            log.info("2. 输入搜索关键词: Playwright Java 自动化测试");

            // 3. 点击搜索按钮（必应搜索按钮 id="sb_form_go"）
            Locator searchBtn = page.locator("#search_icon");
            searchBtn.click();
            log.info("3. 点击搜索按钮");

            // 4. 等待搜索结果加载完成（网络空闲+结果容器出现）
            page.waitForLoadState(LoadState.NETWORKIDLE);
            page.waitForSelector("#b_results", new Page.WaitForSelectorOptions().setTimeout(10000));
            log.info("4. 搜索结果已加载");

            // 5. 获取第一条搜索结果的标题
            Locator firstResult = page.locator("#b_results li.b_algo h2 a").first();
            String firstTitle = firstResult.textContent();
            log.info("5. 第一条结果标题: {}", firstTitle);

            // 6. 断言页面标题包含关键词
            PlaywrightAssertions.assertThat(page).hasTitle(Pattern.compile("Playwright"));
            log.info("6. 断言通过：页面标题包含 'Playwright'");

            // 7. 全屏截图保存
            String screenshotPath = "bing_search_result.png";
            page.screenshot(new Page.ScreenshotOptions()
                    .setPath(Paths.get(screenshotPath))
                    .setFullPage(true));
            log.info("7. 全屏截图已保存: {}", Paths.get(screenshotPath).toAbsolutePath());

            // 8. 获取当前页面 URL
            String currentUrl = page.url();
            log.info("8. 当前页面 URL: {}", currentUrl);

            // 9. 演示：点击第二页（可选）
            Locator nextPage = page.locator("a.sb_pagN");
            if (nextPage.isVisible()) {
                nextPage.click();
                page.waitForLoadState(LoadState.NETWORKIDLE);
                log.info("9. 已点击翻到第二页");
            } else {
                log.info("9. 没有找到下一页按钮，跳过");
            }

            log.info("===== 示例执行完成 =====");
            result.put("success", true);
            result.put("query", "Playwright Java 自动化测试");
            result.put("firstResultTitle", firstTitle);
            result.put("url", currentUrl);
            result.put("screenshotPath", screenshotPath);

        } catch (Exception e) {
            log.error("执行失败", e);
            result.put("success", false);
            result.put("error", e.getMessage());
            throw new RuntimeException("必应搜索自动化失败: " + e.getMessage(), e);
        }
        return result;
    }

    @Override
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
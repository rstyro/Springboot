import com.microsoft.playwright.*
import com.microsoft.playwright.options.BoundingBox
import com.microsoft.playwright.options.WaitForSelectorState
import top.lrshuai.playwright.exception.ServiceException

import javax.imageio.ImageIO
import java.awt.*
import java.awt.image.BufferedImage
import java.util.List
import java.util.Queue
import java.util.regex.Pattern
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class TestLoginScript {

    private static final Logger log = LoggerFactory.getLogger(TestLoginScript.class)
    private static final String LOGIN_URL = "http://10.12.7.198:38080/micrologin/#/homePage"
    private static final int AFTER_LOGIN_WAIT_MS = 2000
    private static final int SLIDER_MAX_ATTEMPTS = 3
    private static final int MIN_SLIDE_DISTANCE = 1
    private static final int MAX_SLIDE_DISTANCE = 260

    // 测试登录功能
    boolean testLogin(String username, String password) {
        try (Playwright playwright = Playwright.create();
             Browser browser = playwright.chromium()
                     .launch(new BrowserType.LaunchOptions()
                             .setHeadless(false)                    // 有头模式
                             .setSlowMo(50)                         // 每个操作延迟50ms
                             .setTimeout(60000)                     // 启动超时60秒
                             .setArgs(java.util.Arrays.asList(      // 浏览器启动参数
                                     "--start-maximized",               // 窗口最大化
                                     "--disable-blink-features=AutomationControlled"  // 隐藏自动化特征
                             ))
                             .setChannel("chrome")                  // 使用系统安装的 Chrome
                             // .setChannel("msedge")               // 使用系统安装的 Edge
                     );
             Page page = browser.newPage()) {

            log.info("开始登录测试，用户名: {}", username)
            page.navigate(LOGIN_URL)
            log.info("首页加载完成，标题: {}", page.title())

            page.click("text=登录")
            log.info("已点击登录按钮，跳转到登录页")

            page.waitForSelector("input[placeholder='请输入用户名']",
                    new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE))
            page.fill("input[placeholder='请输入用户名']", username)
            page.fill("input[placeholder='请输入密码']", password)
            log.info("用户名和密码已填写")


            page.click(".login-button-width")
            log.info("已提交登录表单")

            handleSliderIfPresent(page)

            page.waitForURL(Pattern.compile(".*home*"), new Page.WaitForURLOptions().setTimeout(3000))
            log.info("登录成功，当前页面标题: {}", page.title())
            return true
        } catch (Exception e) {
            log.error("登录测试过程中发生异常", e)
            throw new ServiceException(e.getLocalizedMessage())
        }
    }

    private void handleSliderIfPresent(Page page) {
        Locator sliderLocator = page.locator(".slider")
        try {
            sliderLocator.first().waitFor(new Locator.WaitForOptions().setTimeout(3000))
            log.info("检测到滑块验证，开始处理")
        } catch (TimeoutError e) {
            log.info("未检测到滑块验证，跳过")
            return
        }

        for (int attempt = 1; attempt <= SLIDER_MAX_ATTEMPTS; attempt++) {
            log.info("滑块验证尝试 {}/{}", attempt, SLIDER_MAX_ATTEMPTS)
            double distance = getSliderDistanceByImage(page)
            if (distance < MIN_SLIDE_DISTANCE || distance > MAX_SLIDE_DISTANCE) {
                log.warn("计算出的距离 {} 超出预期范围 [1,260]，使用默认距离 100", distance)
                distance = 100
            }
            boolean success = performDragWithDistance(page, distance)
            if (success) {
                log.info("滑块验证成功")
                page.waitForTimeout(1000)
                return
            } else {
                log.warn("滑块验证失败，刷新验证码后重试")
//                refreshCaptchaIfNeeded(page)
                page.waitForTimeout(1000)
            }
        }
        log.error("滑块验证最终失败，已达到最大重试次数 {}", SLIDER_MAX_ATTEMPTS)
    }

    private boolean performDragWithDistance(Page page, double distance) {
        Random random = new Random()
        try {
            Locator sliderBlock = locateSliderBlock(page)
            if (sliderBlock == null || !sliderBlock.isVisible()) {
                log.warn("未找到可拖动的滑块元素")
                return false
            }
            BoundingBox box = sliderBlock.boundingBox()
            if (box == null) {
                log.warn("无法获取滑块元素的边界")
                return false
            }
            double startX = box.x + box.width / 2
            double startY = box.y + box.height / 2

            page.mouse().move(startX, startY)
            Thread.sleep(100 + random.nextInt(100))
            page.mouse().down()
            Thread.sleep(50 + random.nextInt(50))

            int steps = 30 + random.nextInt(20)
            for (int i = 1; i <= steps; i++) {
                double currentX = startX + (distance * i / steps)
                double randomY = startY + (random.nextDouble() - 0.5) * 4
                page.mouse().move(currentX, randomY)
                Thread.sleep(8 + random.nextInt(5))
            }
            Thread.sleep(50 + random.nextInt(100))
            page.mouse().up()

            page.waitForTimeout(1000)

            if (isCaptchaErrorPresent(page)) {
                log.warn("滑块验证后出现验证码错误提示")
                return false
            }

            if (isAccountError(page) || isLoginSuccess(page)) {
                return true
            }
            return true
        } catch (Exception e) {
            log.error("执行拖动时发生异常", e)
            throw new ServiceException(e.getLocalizedMessage())
        }
    }

    private Locator locateSliderBlock(Page page) {
        String[] selectors = [".slider-mask .block", "#nc_1_n1z", ".nc_iconfont.btn_slide", ".slide-btn"]
        for (String selector : selectors) {
            Locator loc = page.locator(selector)
            if (loc.count() > 0 && loc.first().isVisible()) {
                return loc.first()
            }
        }
        return null
    }

    private void refreshCaptchaIfNeeded(Page page) {
        String[] refreshSelectors = [".el-icon-refresh-right", ".geetest_refresh", ".captcha-refresh", "img[alt='refresh']"]
        for (String selector : refreshSelectors) {
            Locator refreshBtn = page.locator(selector)
            if (refreshBtn.count() > 0 && refreshBtn.first().isVisible()) {
                refreshBtn.first().click()
                log.debug("点击了验证码刷新按钮")
                page.waitForTimeout(500)
                return
            }
        }
        log.info("未找到刷新按钮，等待滑块自动重置")
        page.waitForTimeout(1000)
    }

    private boolean isCaptchaErrorPresent(Page page) {
        Locator errorMsg = page.locator("div.el-message.el-message--error")
        if (errorMsg.count() > 0) {
            String text = errorMsg.first().textContent()
            if (text != null && text.contains("验证码")) {
                log.info("检测到验证码错误信息: {}", text)
                return true
            }
        }
        return false
    }

    private boolean isAccountError(Page page) {
        Locator errorMsg = page.locator("div.el-message.el-message--error")
        if (errorMsg.count() > 0) {
            String text = errorMsg.first().textContent()
            if (text != null && text.contains("用户名")) {
                log.info("检测到用户名或密码错误信息: {}", text)
                throw new ServiceException(text);
            }
        }
        return false
    }

    private boolean isLoginSuccess(Page page) {
        String url = page.url()
        return !url.contains("/login") && url.contains("homePage")
    }

    private double getSliderDistanceByImage(Page page) {
        try {
            ElementHandle trackElement = page.querySelector(".backgroup-img, .slider-track, .img")
            if (trackElement == null) {
                log.warn("未找到滑块背景元素，无法进行图像识别")
                return -1
            }
            byte[] bgBytes = trackElement.screenshot()
            BufferedImage bgImage = ImageIO.read(new ByteArrayInputStream(bgBytes))
            if (bgImage == null) {
                log.warn("背景截图解码失败")
                return -1
            }

            int width = bgImage.getWidth()
            int height = bgImage.getHeight()
            if (width <= 0 || height <= 0) {
                log.warn("背景图片尺寸无效，宽:{} 高:{}", width, height)
                return -1
            }

            final int GRAY_DIFF_THRESHOLD = 40
            final int BRIGHTNESS_MIN = 100
            final int BRIGHTNESS_MAX = 230
            final int MIN_BLOCK_PIXELS = 1000

            boolean[][] isGrayPixel = new boolean[height][width]
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int argb = bgImage.getRGB(x, y)
                    int a = (argb >> 24) & 0xFF
                    int r = (argb >> 16) & 0xFF
                    int g = (argb >> 8) & 0xFF
                    int b = argb & 0xFF

                    int maxC = Math.max(r, Math.max(g, b))
                    int minC = Math.min(r, Math.min(g, b))
                    boolean isGray = (maxC - minC <= GRAY_DIFF_THRESHOLD) &&
                            (maxC >= BRIGHTNESS_MIN) &&
                            (maxC <= BRIGHTNESS_MAX) &&
                            (a > 120)

                    isGrayPixel[y][x] = isGray
                }
            }

            boolean[][] visited = new boolean[height][width]
            List<SliderBlock> blocks = new ArrayList<>()

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (isGrayPixel[y][x] && !visited[y][x]) {
                        Queue<Point> queue = new LinkedList<>()
                        queue.add(new Point(x, y))
                        visited[y][x] = true

                        int minX = x, maxX = x
                        int minY = y, maxY = y
                        int pixelCount = 0

                        while (!queue.isEmpty()) {
                            Point p = queue.poll()
                            pixelCount++
                            minX = Math.min(minX, p.x)
                            maxX = Math.max(maxX, p.x)
                            minY = Math.min(minY, p.y)
                            maxY = Math.max(maxY, p.y)

                            int[] dx = [-1, 1, 0, 0]
                            int[] dy = [0, 0, -1, 1]
                            for (int i = 0; i < 4; i++) {
                                int nx = p.x + dx[i]
                                int ny = p.y + dy[i]
                                if (nx >= 0 && nx < width && ny >= 0 && ny < height &&
                                        isGrayPixel[ny][nx] && !visited[ny][nx]) {
                                    visited[ny][nx] = true
                                    queue.add(new Point(nx, ny))
                                }
                            }
                        }

                        if (pixelCount >= MIN_BLOCK_PIXELS) {
                            blocks.add(new SliderBlock(minX, maxX, minY, maxY, pixelCount))
                        }
                    }
                }
            }

            if (blocks.isEmpty()) {
                log.warn("未识别到符合条件的灰色滑块块")
                return -1
            }

            SliderBlock targetBlock = blocks.stream()
                    .max(Comparator.comparingInt { it.pixelCount })
                    .orElse(null)

            if (targetBlock == null) {
                log.warn("未找到有效滑块块")
                return -1
            }

            double sliderCenterX = (targetBlock.minX + targetBlock.maxX) / 2.0
            double distance = sliderCenterX - 30

            distance = Math.max(MIN_SLIDE_DISTANCE, Math.min(MAX_SLIDE_DISTANCE, distance))

            log.info("灰色滑块识别结果：滑块块区域[minX={}, maxX={}, minY={}, maxY={}]，像素数={}，中心X={}，计算距离={}px",
                    targetBlock.minX, targetBlock.maxX, targetBlock.minY, targetBlock.maxY,
                    targetBlock.pixelCount, sliderCenterX, distance)

            return distance
        } catch (Exception e) {
            log.error("图像识别滑块距离失败", e)
            return -1
        }
    }

    private static class SliderBlock {
        int minX, maxX, minY, maxY
        int pixelCount

        SliderBlock(int minX, int maxX, int minY, int maxY, int pixelCount) {
            this.minX = minX
            this.maxX = maxX
            this.minY = minY
            this.maxY = maxY
            this.pixelCount = pixelCount
        }
    }
}
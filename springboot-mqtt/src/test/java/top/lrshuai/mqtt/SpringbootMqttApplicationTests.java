package top.lrshuai.mqtt;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StringUtils;
import top.lrshuai.mqtt.config.MqttClientManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

@Slf4j
@SpringBootTest
class SpringbootMqttApplicationTests {

    @Resource
    private MqttClientManager mqttClientManager;

    // 用于等待接收到消息的同步工具
    private final CountDownLatch shutdownLatch  = new CountDownLatch(1);

    @Test
    public void testPublishAndSubscribe() throws InterruptedException {
        // 开始一个新的线程来处理控制台输入
        Thread consoleInputThread = new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
                String line;
                while ((line = reader.readLine()) != null && !Thread.currentThread().isInterrupted()) {
                    if ("exit".equalsIgnoreCase(line.trim())) {
                        mqttClientManager.destroy();
                        shutdownLatch.countDown();
                        break;
                    } else if (!StringUtils.isEmpty(line)) {
                        publishMessage(line);
                    }
                }
            } catch (Exception e) {
                log.warn("控制台输入处理出错: {}", e.getMessage());
            }
        });
        consoleInputThread.start();
        // 等待最多 10 秒钟，看是否能接收到消息
        shutdownLatch .await();

    }

    /**
     * 发布一条消息到默认主题
     *
     * @param payload 输入的消息内容
     */
    private void publishMessage(String payload) {
        String clientName = "client1"; // 根据你的配置调整
        String topic = "test/topic";
        int qos = 1; // 质量服务等级
        Map<String, Object> data = new HashMap<>();
        data.put("payload", payload);
        data.put("cmd", "test");
        try {
            mqttClientManager.publish(clientName, topic, data, qos);
            log.info("\uD83D\uDE80已发布消息到主题: {}, 内容: {}", topic, payload);
        } catch (Exception e) {
            log.error("发布消息失败: {}", e.getMessage());
        }
    }

}

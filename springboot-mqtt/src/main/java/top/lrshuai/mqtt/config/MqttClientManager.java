package top.lrshuai.mqtt.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class MqttClientManager {

    // 存储所有客户端实例
    private final Map<String, MqttClient> clients = new ConcurrentHashMap<>();

    private final MqttClientProperties mqttProperties;
    private final ObjectMapper objectMapper;

    @Autowired
    public MqttClientManager(MqttClientProperties mqttProperties, ObjectMapper objectMapper) {
        this.mqttProperties = mqttProperties;
        this.objectMapper = objectMapper;
    }

    // 应用启动完成后初始化MQTT客户端
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        initializeClientsWithRetry(mqttProperties.getMaxRetries());
    }

    private void initializeClientsWithRetry(int maxRetries) {
        mqttProperties.getClients().forEach((name, config) -> {
            int attempt = 0;
            while (attempt < maxRetries) {
                try {
                    MqttClient client = new MqttClient(
                            config.getBrokerUrl(),
                            config.getClientId(),
                            new MemoryPersistence()
                    );
                    MqttConnectOptions options = getMqttConnectOptions(config);
                    setupClientCallback(client, name, config.getTopics());
                    client.connect(options);
                    clients.put(name, client);
                    break; // 成功退出循环
                } catch (MqttException e) {
                    handleInitializationFailure(name, ++attempt, maxRetries, e);
                }
            }
        });
    }

    /**
     * 处理初始化失败的逻辑，包括重试机制
     */
    private void handleInitializationFailure(String clientName, int attempt, int maxRetries, Exception e) {
        log.error("初始化 MQTT 客户端 [{}] 失败，尝试次数：{}", clientName, attempt, e);
        if (attempt >= maxRetries) {
            log.error("MQTT 客户端 [{}] 达到最大重试次数 {}，初始化失败。", clientName, maxRetries);
        } else {
            int delay = Math.min(30, (int) Math.pow(2, attempt)); // 最大延迟30秒
            log.warn("将在 {} 秒后重试 MQTT 客户端 [{}] 初始化...", delay, clientName);

            try {
                Thread.sleep(delay * 1000L);
            } catch (InterruptedException ie) {
                log.warn("等待重试时被中断", ie);
                Thread.currentThread().interrupt(); // 恢复中断状态
            }
        }
    }

    /**
     * 设置客户端回调逻辑
     */
    private void setupClientCallback(MqttClient client, String clientName, List<MqttClientProperties.TopicConfig> topics) {
        client.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                log.info("MQTT 连接成功: {}", serverURI);
                if (reconnect) {
                    log.info("MQTT 客户端[{}]断开后重连成功", clientName);
                    resubscribeTopics(client, clientName, topics);
                } else {
                    log.info("首次连接到 Broker: {}", serverURI);
                    subscribeTopics(client, clientName, topics);
                }
            }

            @Override
            public void connectionLost(Throwable cause) {
                log.error("MQTT 连接丢失: {}", cause.getMessage(), cause);
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                handleMessage(topic, message);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                log.debug("消息发送完成");
            }
        });
    }

    private static MqttConnectOptions getMqttConnectOptions(MqttClientProperties.ClientConfig config) {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(config.getUsername());
        options.setPassword(config.getPassword().toCharArray());
        options.setAutomaticReconnect(true); // 启用自动重连
        options.setCleanSession(false); // 设置为false以保留会话状态
        return options;
    }

    private void subscribeTopics(MqttClient client, String name, List<MqttClientProperties.TopicConfig> topics) {
        for (MqttClientProperties.TopicConfig topic : topics) {
            try {
                client.subscribe(topic.getTopic(), topic.getQos());
                log.info("客户端 [{}] 已成功订阅主题: {}", name, topic.getTopic());
            } catch (MqttException e) {
                log.error("客户端 [{}] 订阅主题 [{}] 失败", name, topic.getTopic(), e);
            }
        }
    }

    private void resubscribeTopics(MqttClient client, String name, List<MqttClientProperties.TopicConfig> topics) {
        subscribeTopics(client, name, topics);
    }

    private void handleMessage(String topic, MqttMessage message) {
        try {
            String payload = new String(message.getPayload(), StandardCharsets.UTF_8);
            log.info("收到消息 - 主题: {}, 内容: {}", topic, payload);
            // todo 业务处理
        } catch (Exception e) {
            log.error("解析 MQTT 消息出错: {}", e.getMessage(), e);
        }
    }


    public void publish(String clientName, String topic, Object payload, int qos) {
        publish(clientName, topic, payload, qos, Boolean.TRUE);
    }
    /**
     * 向指定客户端和主题发布消息
     * @param clientName 客户端名称（配置中定义）
     * @param topic      主题名称
     * @param payload    要发布的对象内容（将自动转换为 JSON）
     * @param qos        消息质量等级（0, 1, 2）
     */
    public void publish(String clientName, String topic, Object payload, int qos,boolean retained) {
        MqttClient client = clients.get(clientName);
        if (client != null && client.isConnected()) {
            try {
                // 使用 objectMapper 将对象转为 JSON 字符串，并编码为字节数组
                String jsonPayload = objectMapper.writeValueAsString(payload);
                /**
                 * retained 参数在 MQTT 协议中用于控制消息的保留标志。
                 * 当设置为 true 时，这意味着发布的消息将被 MQTT Broker 保留，并且该主题下的所有后续订阅者（包括当前已经订阅的和未来将要订阅的）都会立即收到这条保留消息。
                 * 如果设置为 false，则消息不会被保留，Broker 不会存储这条消息，也不会将其发送给新的订阅者。
                 * 注意事项:
                 * 仅最新的一条保留消息会被保存：对于每个主题，Broker 只会保存最新的那条带有保留标志的消息。
                 * 如果再次发布相同主题的消息且 retained=true，旧的保留消息会被覆盖。
                 */
                client.publish(topic, jsonPayload.getBytes(StandardCharsets.UTF_8), qos,retained);
                log.info("消息已发布到主题: {}, 内容: {}", topic, jsonPayload);
            } catch (JsonProcessingException e) {
                log.error("MQTT 消息序列化失败", e);
            } catch (MqttException e) {
                log.error("发布 MQTT 消息失败: {}", e.getMessage(), e);
            }
        } else {
            log.warn("MQTT 客户端 [{}] 不在线，无法发布消息到主题 [{}]", clientName, topic);
        }
    }

    // ========== 健康检查与自动重连 ==========
    @Scheduled(fixedRate = 60_000)
    public void checkConnections() {
        log.info("健康检查与自动重连");
        clients.forEach((name, client) -> {
            if (!client.isConnected()) {
                log.warn("MQTT 客户端 [{}] 当前未连接，尝试重新连接", name);
                try {
                    client.reconnect();
                } catch (MqttException e) {
                    log.error("手动重连失败", e);
                }
            }
        });
    }

    // ========== 资源释放 ==========
    @PreDestroy
    public void destroy() {
        clients.forEach((name, client) -> {
            try {
                client.disconnect();
                log.info("MQTT 客户端 [{}] 已断开连接", name);
            } catch (MqttException e) {
                log.warn("关闭 MQTT 客户端 [{}] 时发生错误", name, e);
            }
        });
    }
}

package top.lrshuai.mqtt.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "mqtt")
public class MqttClientProperties {
    /**
     * 最大重试次数
     */
    private int maxRetries=10;
    /**
     * 多个MQTT客户端map
     */
    private Map<String, ClientConfig> clients;

    @Data
    public static class ClientConfig  {
        // MQTT服务地址
        private String brokerUrl;
        // 客户端ID
        private String clientId;
        // 认证的账号密码
        private String username;
        private String password;
        // 多个主题
        private List<TopicConfig> topics;
    }

    @Data
    public static class TopicConfig {
        // 主题
        private String topic;
        /**
         * 消息传递的保证程度：
         * 0=最多一次，消息不会被确认。不会有重发机制
         * 1=至少一次，确保消息至少会被送达一次，但也可能多次。这意味着接收者可能会收到重复的消息
         * 2=恰好一次，提供了最高的消息传递保证，确保每条消息仅被接收者准确地接收一次。这是最安全但也是最耗资源的方式
         *      使用两阶段握手协议来确保消息的唯一性和可靠性。
         *      首先发送PUBLISH消息并等待PUBREC响应，然后发送PUBREL消息并等待PUBCOMP响应。
         *      适用于不允许消息丢失或重复的关键性应用
         */
        private int qos;
    }
}

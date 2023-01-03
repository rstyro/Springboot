package top.rstyro.poetry.config;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Objects;

/**
 * elasticsearch 客户端配置
 */
@ConditionalOnProperty(prefix = "elasticsearch",name = "enable",havingValue = "true")
@Configuration
public class ElasticsearchConfiguration {
    /**
     * elasticsearch 连接地址多个地址使用,分隔
     */
    @Value("${elasticsearch.hosts}")
    private String hosts;

    /**
     * es 用户名
     */
    @Value("${elasticsearch.username}")
    private String username;

    /**
     * es 密码
     */
    @Value("${elasticsearch.password}")
    private String password;

    /**
     * 连接目标url最大超时
     */
    @Value("${elasticsearch.connectTimeOut}")
    private Integer connectTimeOut;

    /**
     * 等待响应（读数据）最大超时
     */
    @Value("${elasticsearch.socketTimeOut}")
    private Integer socketTimeOut;

    /**
     * 从连接池中获取可用连接最大超时时间
     */
    @Value("${elasticsearch.connectionRequestTime}")
    private Integer connectionRequestTime;

    /**
     * 默认：DefaultConnectionKeepAliveStrategy 返回-1 （无限制）
     * 服务器 keepAlive短于 客户端：会报 java.io.IOException: Connection reset by peer
     * httpclient5 DEFAULT_CONN_KEEP_ALIVE = TimeValue.ofMinutes(3); 3分钟
     */
    private final static long KEEP_ALIVE=180000;

    /**
     * 构建RestHighLevelClient
     * @return
     */
    @Bean(destroyMethod = "close")
    public RestHighLevelClient restHighLevelClient() {
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
        HttpHost[] httpHosts = Arrays.stream(hosts.split(",")).map(host -> new HttpHost(host.split(":")[0], Integer.parseInt(host.split(":")[1]))).filter(Objects::nonNull).toArray(HttpHost[]::new);
        RestClientBuilder builder = RestClient.builder(httpHosts)
                .setHttpClientConfigCallback(httpClientBuilder -> {
                    RequestConfig.Builder requestConfigBuilder = RequestConfig.custom()
                            .setConnectTimeout(connectTimeOut)
                            .setSocketTimeout(socketTimeOut)
                            .setConnectionRequestTimeout(connectionRequestTime);
                    httpClientBuilder.setDefaultRequestConfig(requestConfigBuilder.build());
                    httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                    httpClientBuilder.setKeepAliveStrategy((response, context) -> KEEP_ALIVE);
                    return httpClientBuilder;
                });
        return new RestHighLevelClient(builder);
    }

}

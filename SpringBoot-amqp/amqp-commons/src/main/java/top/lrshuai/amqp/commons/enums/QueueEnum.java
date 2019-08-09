package top.lrshuai.amqp.commons.enums;

public enum QueueEnum {
    /**
     * 测试消息队列
     */
    TEST_MESSAGE("test.message.queue", "test.message");
    /**
     * 队列名称
     */
    private String name;
    /**
     * 队列路由键
     */
    private String routingKey;

    public String getName() {
        return name;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    QueueEnum(String name, String routingKey) {
        this.name = name;
        this.routingKey = routingKey;
    }
}

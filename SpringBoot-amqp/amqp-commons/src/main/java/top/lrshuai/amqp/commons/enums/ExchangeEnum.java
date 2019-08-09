package top.lrshuai.amqp.commons.enums;

public enum ExchangeEnum {
    /**
     * 测试交换机枚举
     */
    TEST_MESSAGE("test.message.exchange");
    private String value;

    public String getValue() {
        return value;
    }

    ExchangeEnum(String value) {
        this.value = value;
    }
}

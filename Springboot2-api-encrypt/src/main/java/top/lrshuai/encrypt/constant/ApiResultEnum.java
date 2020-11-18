package top.lrshuai.encrypt.constant;

/**
 * @author rstyro
 */

public enum ApiResultEnum {
    /**
     * 常见错误
     */
    SUCCESS("200", "ok"),

    ;

    private String message;
    private String status;

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

    private ApiResultEnum(String status, String message) {
        this.message = message;
        this.status = status;
    }


}

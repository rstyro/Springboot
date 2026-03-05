package top.lrshuai.keycloak.exception;


/**
 * 业务异常
 */
public class ServiceException extends RuntimeException {

    /**
     * 错误码
     */
    private Integer code = -1;

    /**
     * 错误提示
     */
    private String message;
    /**
     * 错误明细，内部调试错误
     */
    private String detailMessage;


    public String getDetailMessage() {
        return detailMessage;
    }

    /**
     * 空构造方法，避免反序列化问题
     */
    public ServiceException() {

    }

    public ServiceException(String message) {
        this.message = message;
        this.code = 500;
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
        this.code = 500;
    }

    public ServiceException(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    public ServiceException(Integer code, String message) {
        this.message = message;
        this.code = code;
    }

    public ServiceException setMessage(String message) {
        this.message = message;
        return this;
    }

    public ServiceException setDetailMessage(String detailMessage) {
        this.detailMessage = detailMessage;
        return this;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }
}

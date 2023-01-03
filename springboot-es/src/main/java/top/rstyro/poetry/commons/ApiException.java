package top.rstyro.poetry.commons;

import lombok.Data;

/**
 * 自定义的api异常
 * @author rstyro
 *
 */
@Data
public class ApiException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    private int status;
    private String message;
    private Object data;
    private Exception exception;
    private Object[] parameters;
    public ApiException() {
        super();
    }
    public ApiException(int status, String message, Object data,Object[] parameters, Exception exception) {
        this.status = status;
        this.parameters=parameters;
        for (int i = 0; this.parameters != null && i < this.parameters.length; i++) {
            message = message.replace("{" + i +"}", this.parameters[i].toString());
        }
        this.message = message;
        this.data = data;
        this.exception = exception;
    }
    public ApiException(ApiExceptionCode apiExceptionCode) {
        this(apiExceptionCode.getStatus(),apiExceptionCode.getMessage(),null,null,null);
    }
    public ApiException(ApiExceptionCode apiExceptionCode,Object... parameters) {
        this(apiExceptionCode.getStatus(),apiExceptionCode.getMessage(),null,parameters,null);
    }
    public ApiException(ApiExceptionCode apiExceptionCode, Object data, Exception exception) {
        this(apiExceptionCode.getStatus(),apiExceptionCode.getMessage(),data,null,exception);
    }

}
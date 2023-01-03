package top.rstyro.poetry.commons;

import lombok.Data;
import top.rstyro.poetry.util.ContextUtil;

import java.io.Serializable;

/**
 * 自定义的接口返回实体类
 * @author rstyro
 *
 */
@Data
public class R<T> implements Serializable {
    private int code;
    private String msg;
    private String trackerId;
    private T data;

    public R(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.trackerId = ContextUtil.getTrackerId();
        this.data = data;
    }

    public static <E> R<E> success() {
        return new R<>(ApiExceptionCode.SUCCESS.getStatus(), ApiExceptionCode.SUCCESS.getMessage(), null);
    }
    public static <E> R<E> success(E data) {
        return new R<>(ApiExceptionCode.SUCCESS.getStatus(), ApiExceptionCode.SUCCESS.getMessage(), data);
    }

    public static <E> R<E> fail(ApiExceptionCode apiExceptionCode) {
        return new R<>(apiExceptionCode.getStatus(), apiExceptionCode.getMessage(), null);
    }
    public static <E> R<E> fail(ApiExceptionCode apiExceptionCode,Object[] parameters) {
        String message = apiExceptionCode.getMessage();
        for (int i = 0; parameters != null && i < parameters.length; i++) {
            message = message.replace("{" + i +"}", parameters[i].toString());
        }
        return new R<>(apiExceptionCode.getStatus(), message, null);
    }

    public static <E> R<E> fail(String msg) {
        return new R<>(-1, msg, null);
    }
    public static <E> R<E> fail(int code,String msg) {
        return new R<>(-1, msg, null);
    }
}

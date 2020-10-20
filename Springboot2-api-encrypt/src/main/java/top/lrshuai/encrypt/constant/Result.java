package top.lrshuai.encrypt.constant;

import java.util.HashMap;
import java.util.Map;


/**
 * 返回值model
 *
 * @author rstyro
 */
public class Result extends HashMap<String, Object> {
    /**
     * 状态
     */
    public static final String STATUS="status";
    /**
     * 信息
     */
    public static final String MESSAGE="message";
    /**
     * 数据体
     */
    public static final String DATA="data";
    /**
     * 密钥
     */
    public static final String KEY="key";

    private static final long serialVersionUID = 1L;

    public Result() {
        put(STATUS, 200);
        put(MESSAGE, "ok");
    }

    public static Result error() {
        return error("500", "系统错误，请联系管理员");
    }

    public static Result error(String msg) {
        return error("500", msg);
    }

    public static Result error(ApiResultEnum resultEnum) {
        Result r = new Result();
        r.put("status", resultEnum.getStatus());
        r.put("message", resultEnum.getMessage());
        return r;
    }

    public static Result error(String status, String msg) {
        Result r = new Result();
        r.put(STATUS, status);
        r.put(MESSAGE, msg);
        return r;
    }

    public static Result ok(Map<String, Object> map) {
        Result r = new Result();
        r.putAll(map);
        return r;
    }

    public static Result ok(Object data) {
        Result r = new Result();
        r.put(DATA, data);
        return r;
    }

    public static Result ok(Object data,String key) {
        Result r = new Result();
        r.put(DATA, data);
        r.put(KEY, key);
        return r;
    }

    public static Result ok() {
        return new Result();
    }

    @Override
    public Result put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
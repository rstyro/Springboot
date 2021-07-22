package top.lrshuai.security.commons;

import java.util.HashMap;
import java.util.Map;

/**
 * 接口返回 module
 */
public class R extends HashMap<String, Object> {

	private static final long serialVersionUID = 1L;
	private static final String STATUS = "status";
	private static final String MESSAGE = "message";
	private static final String DATA = "data";

	public R() {
		put(STATUS, 200);
		put(MESSAGE, "ok");
	}

	public static R error() {
		return error("500", "系统错误，请联系管理员");
	}

	public static R error(String msg) {
		return error("500", msg);
	}

	public static R error(String status, String msg) {
		R r = new R();
		r.put(STATUS, status);
		r.put(MESSAGE, msg);
		return r;
	}

	public static R error(ApiResultEnum resultEnum) {
		R r = new R();
		r.put(STATUS, resultEnum.getStatus());
		r.put(MESSAGE, resultEnum.getMessage());
		return r;
	}

	public static R ok(Map<String, Object> map) {
		R r = new R();
		r.putAll(map);
		return r;
	}
	public static R ok(Object data) {
		R r = new R();
		r.put(DATA,data);
		return r;
	}

	public static R ok() {
		return new R();
	}

	@Override
	public R put(String key, Object value) {
		super.put(key, value);
		return this;
	}
}
package top.lrshuai.jwt.common;

import java.util.HashMap;
import java.util.Map;


public class Result extends HashMap<String, Object> {

	private static final long serialVersionUID = 1L;

	public Result() {
		put("status", 200);
		put("message", "ok");
	}

	public static Result error() {
		return error("500", "系统错误，请联系管理员");
	}

	public static Result error(String msg) {
		return error("500", msg);
	}

	public static Result error(String status, String msg) {
		Result r = new Result();
		r.put("status", status);
		r.put("message", msg);
		return r;
	}

	public static Result error(ApiResultEnum resultEnum) {
		Result r = new Result();
		r.put("status", resultEnum.getStatus());
		r.put("message", resultEnum.getMessage());
		return r;
	}

	public static Result ok(Map<String, Object> map) {
		Result r = new Result();
		r.putAll(map);
		return r;
	}
	public static Result ok(Object data) {
		Result r = new Result();
		r.put("data",data);
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
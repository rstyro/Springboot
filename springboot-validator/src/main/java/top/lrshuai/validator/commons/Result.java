package top.lrshuai.validator.commons;

import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;


/**
 * 随便封装了下接口返回 模板类
 */
public class Result extends HashMap<String, Object> {

	private static final long serialVersionUID = 1L;

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
		r.put(DATA,data);
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

	public Result putData(String key, Object value) {
		Object data = getData();
		if(ObjectUtils.isEmpty(data)){
			data=new HashMap<String,Object>();
			this.put(DATA,data);
		}
		if(data instanceof HashMap){
			((HashMap) data).put(key,value);
		}else {
			throw new RuntimeException("不是map类型，无法添加其他属性");
		}
		return this;
	}

	public Object getData(){
		return this.get(DATA);
	}

}

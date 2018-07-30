package top.lrshuai.i18n.constant;

public enum ResponseCodeEnum {
	SUCCESS("200","成功"),
	NOT_AUTH("403","没有权限"),
	NOT_FOUND("404","没有找到"),
	FAILED("500","失败");
	
	private String code;
	private String msg;
	private ResponseCodeEnum(String code,String msg) {
		this.code=code;
		this.msg=msg;
	}
	
	public String getCode() {
		return code;
	}
	public String getMsg() {
		return msg;
	}
	
}

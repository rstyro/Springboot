package top.lrshuai.security.commons;

public enum ApiResultEnum {
	SUCCESS("200","ok"),
	FAILED("400","请求失败"),
	ERROR("500","服务繁忙"),
	TOKEN_USER_INVALID("70000","Token过期或用户未登录"),
	;
	
	private String message;
	private String status;
	
	public String getMessage() {
		return message;
	}

	public String getStatus() {
		return status;
	}
	private ApiResultEnum(String status,String message) {
		this.message = message;
		this.status = status;
	}

	
}

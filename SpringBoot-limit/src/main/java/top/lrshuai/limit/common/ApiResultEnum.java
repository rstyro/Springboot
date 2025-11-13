package top.lrshuai.limit.common;

public enum ApiResultEnum {
	SUCCESS(200,"ok"),
	FAILED(400,"请求失败"),
	ERROR(500,"不知名错误"),
	ERROR_NULL(501,"空指针异常"),
	ERROR_CLASS_CAST(502,"类型转换异常"),
	ERROR_RUNTIME(503,"运行时异常"),
	ERROR_IO(504,"上传文件异常"),
	ERROR_MONTH_NOT_SUPPORT(505,"请求方法错误"),


	REQUEST_LIMIT(10001,"请求次数受限"),
	;
	
	private String message;
	private int status;
	
	public String getMessage() {
		return message;
	}

	public int getStatus() {
		return status;
	}
	private ApiResultEnum(int status, String message) {
		this.message = message;
		this.status = status;
	}

	
}

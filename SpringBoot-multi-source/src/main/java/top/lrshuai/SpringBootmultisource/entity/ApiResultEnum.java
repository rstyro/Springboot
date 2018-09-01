package top.lrshuai.SpringBootmultisource.entity;

public enum ApiResultEnum {
	SUCCESS(200,"ok"),
	ERROR(500,"不知名错误"),
	ERROR_NULL(501,"空指针异常"),
	ERROR_CLASS_CAST(502,"类型转换异常"),
	ERROR_RUNTION(503,"运行时异常"),
	ERROR_IO(504,"上传文件异常"),
	ERROR_MOTHODNOTSUPPORT(505,"请求方法错误"),
	FAILED(601,"请求失败"),
	PARAMETER_NULL(1001,"缺少参数或值为空"),
	;
	
	private String message;
	private int status;
	
	public String getMessage() {
		return message;
	}

	public int getStatus() {
		return status;
	}
	private ApiResultEnum(int status,String message) {
		this.message = message;
		this.status = status;
	}

	
}

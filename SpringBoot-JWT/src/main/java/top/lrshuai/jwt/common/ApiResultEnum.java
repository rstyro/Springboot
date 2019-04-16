package top.lrshuai.jwt.common;

public enum ApiResultEnum {
	SUCCESS("200","ok"),
	FAILED("400","请求失败"),
	ERROR("500","不知名错误"),
	ERROR_NULL("501","空指针异常"),
	ERROR_CLASS_CAST("502","类型转换异常"),
	ERROR_RUNTION("503","运行时异常"),
	ERROR_IO("504","上传文件异常"),
	ERROR_MOTHODNOTSUPPORT("505","请求方法错误"),







	TOKEN_EXPIRED("10001","token 过期"),
	SIGN_VERIFI_ERROR("10002","签名不匹配"),
	ALGORITHM_CAN_NOT_NULL("10003","加密方式不能为空，可选 RS256、HS256"),



	;
	
	private String message;
	private String status;
	
	public String getMessage() {
		return message;
	}

	public String getStatus() {
		return status;
	}
	private ApiResultEnum(String status, String message) {
		this.message = message;
		this.status = status;
	}

	
}

package top.lrshuai.i18n.dto;

import top.lrshuai.i18n.constant.ResponseCodeEnum;
/**
 * 接口返回 传输对象体
 * @author rstyro
 *
 */
public class ResponseDTO {
	private String resultCode;
	private String resultMsg;
	private Object resultData;
	
	public ResponseDTO() {}
	
	public ResponseDTO(String resultCode, String resultMsg, Object resultData) {
		super();
		this.resultCode = resultCode;
		this.resultMsg = resultMsg;
		this.resultData = resultData;
	}
	public ResponseDTO(ResponseCodeEnum codeEnum, Object resultData) {
		this(codeEnum.getCode(),codeEnum.getMsg(),resultData);
	}

	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getResultMsg() {
		return resultMsg;
	}
	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}
	public Object getResultData() {
		return resultData;
	}
	public void setResultData(Object resultData) {
		this.resultData = resultData;
	}
	
}

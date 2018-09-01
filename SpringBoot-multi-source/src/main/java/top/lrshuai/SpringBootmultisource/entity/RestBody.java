package top.lrshuai.SpringBootmultisource.entity;

public class RestBody {
	private String message;
	private int status;
	private Object data;
	public RestBody() {
		super();
	}
	public RestBody( int status,String message, Object data) {
		super();
		this.message = message;
		this.status = status;
		this.data = data;
	}
	public RestBody(ApiResultEnum api,Object data){
		this( api.getStatus(),api.getMessage(), data);
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
}

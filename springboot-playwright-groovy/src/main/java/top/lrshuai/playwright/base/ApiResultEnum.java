package top.lrshuai.playwright.base;

public enum ApiResultEnum {
	SUCCESS(200,"ok"),
	FAILED(400,"请求失败"),
	NO_AUTH(403,"您无权限访问"),
	ERROR(500,"系统错误，请稍后重试"),
	ERROR_NULL(501,"空指针异常"),
	ERROR_CLASS_CAST(502,"类型转换异常"),
	ERROR_RUN(503,"运行时异常"),
	ERROR_IO(504,"上传文件异常"),
	ERROR_REQUEST_ERR(505,"请求方法错误"),

	ERROR_REQUEST_DECODE_FAIL(506,"参数解密失败"),
	ERROR_ENCRYPT_KEY_IS_NULL(507,"约定的密钥KEY为空"),
	ERROR_INVALID_PARAM(508,"参数错误:%s"),

	/**系统框架，报错code:1000-2000 */
	SYSTEM_CODE_ERROR(1000,"验证码错误"),
	SYSTEM_ACCOUNT_NOT_FOUND(1001,"账号或密码错误"),
	SYSTEM_PASSWORD_ERROR(1002,"账号或密码错误"),
	SYSTEM_USER_NOT_FOUND(1003,"用户找不到"),
	SYSTEM_USER_EXIST(1004,"用户名已存在"),
	SYSTEM_USER_ABOVE_MAX_RETRY_COUNT(1005,"用户登录错误次数过多已被锁定，请稍后再试"),


	// APP相关
	APP_USER_NO_LOGIN_OR_EXPIRED(2001,"用户未登录或登录已过期"),
	APP_USER_NICK_NAME_EXIST(2002,"用户昵称已被使用"),
	APP_USER_NOT_FOUND(2003,"用户不存在"),

	// 小程序相关
	MINI_USER_NO_LOGIN_OR_EXPIRED(40001,"用户未登录或登录已过期"),
	MINI_AI_MESSAGE(40002,"消息不存在或已过期"),
	MINI_ORDER_NUMBER_ISNULL(40003,"订单号或ID为空"),
	MINI_ORDER_STOCK_INSUFFICIENT(40004,"商品库存不足"),
	MINI_ORDER_PRODUCT_NOT_FOUND(40005,"商品已下架或不存在"),
	MINI_ORDER_PRODUCT_UPDATE_STOCK_FAIL(40006,"更新商品库存失败"),
	MINI_ORDER_NOT_FOUND(40007,"订单不存在或已过期"),
	MINI_USER_AVATAR_URL_IS_NULL(40009,"用户头像地址为空"),
	MINI_USER_NICK_NAME_EXIST(40010,"用户昵称已被使用"),
	MINI_USER_SEND_EMAIL_FAILD(40011,"发送邮件失败"),
	MINI_USER_EMAIL_FORMAT_ERROR(40012,"邮件格式不正确"),
	MINI_USER_EXIST(40013,"用户已存在，请直接登录"),
	MINI_EXCHANGE_CODE_EXIST(40014,"已兑换过物品"),
	MINI_EXCHANGE_CODE_IS_NULL(40015,"兑换码不存在或已失效"),
	MINI_ACHIEVEMENT_NOT_FOUND(40016,"成就奖励已失效或不存在"),
	MINI_ACHIEVEMENT_UNMET_CLAIM_REWARD(40017,"未满足领取条件"),
	MINI_REPEAT_CLAIM_REWARD(40018,"不能重复领取奖励"),
	;

	private String message;
	private Integer code;

	public String getMessage() {
		return message;
	}

	public Integer getCode() {
		return code;
	}
	private ApiResultEnum(Integer code, String message) {
		this.message = message;
		this.code = code;
	}


}

package top.lrshuai.amqp.commons.constant;

public enum ApiResultEnum {
    SUCCESS("200", "ok"),
    FAILED("400", "请求失败"),
    ERROR("500", "不知名错误"),
    ERROR_NULL("501", "空指针异常"),
    ERROR_CLASS_CAST("502", "类型转换异常"),
    ERROR_RUNTION("503", "运行时异常"),
    ERROR_IO("504", "上传文件异常"),
    ERROR_MOTHODNOTSUPPORT("505", "请求方法错误"),
    ERROR_BY_UPDATE_TRY("506", "重试失败"),


    //参数
    PARAMETER_NULL("10001", "缺少参数或值为空"),
    PARAMETER_NOT_FOUND("10002", "找不到业务数据"),
    PARAMETER_IP_EXIST("10003", "该IP服务器地址已存在"),
    UPDATE_IS_FAILD("10004", "更新失败了，请重试"),


    //账户
    ACCOUNT_LOCK("20001", "账号已锁定"),
    ACCOUNT_NOT_FOUND("20002", "找不到账户信息"),
    ACCOUNT_PASSWARD_ERROR("20003", "用户名密码错误"),
    ACCOUNT_EXIST("20004", "账号已存在"),

    //权限
    AUTH_NOT_HAVE("30001", "没有权限"),


    FILE_IS_NULL("40001", "文件为空"),


    CONFIG_IS_EXIST("50001", "配置已存在"),
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

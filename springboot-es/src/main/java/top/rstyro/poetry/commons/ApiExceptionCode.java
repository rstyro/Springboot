package top.rstyro.poetry.commons;

public enum ApiExceptionCode {
    // 基础报错code
    SUCCESS(200,"成功"),
    FAILED(400,"请求失败"),
    ERROR(500,"不知名错误"),
    ERROR_NULL(501,"空指针异常"),
    ERROR_CLASS_CAST(502,"类型转换异常"),
    ERROR_RUNTIME(503,"运行时异常"),
    ERROR_IO(504,"上传文件异常"),
    ERROR_METHOD(505,"请求方法错误"),


    // 业务code
    ES_OVER_MAX_RESULT(10001,"超过最大分页限制");

    ;
    private String message;
    private int status;

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    ApiExceptionCode( int status,String message) {
        this.message = message;
        this.status = status;
    }
}

package top.lrshuai.limit.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.lrshuai.limit.common.ApiException;
import top.lrshuai.limit.common.ApiResultEnum;
import top.lrshuai.limit.common.R;

import java.io.IOException;

/**
 * 全局异常捕获
 * @author rstyro
 * @since 2019-03-12
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(NullPointerException.class)
	public R NullPointer(NullPointerException ex){
	   logger.error(ex.getMessage(),ex);
	   return R.fail(ApiResultEnum.ERROR_NULL);
    }

    @ExceptionHandler(ClassCastException.class)
    public R ClassCastException(ClassCastException ex){
        logger.error(ex.getMessage(),ex);
        return R.fail(ApiResultEnum.ERROR_CLASS_CAST);
    }

    @ExceptionHandler(IOException.class)
    public R IOException(IOException ex){
        logger.error(ex.getMessage(),ex);
        return R.fail(ApiResultEnum.ERROR_IO);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public R HttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex){
        logger.error(ex.getMessage(),ex);
        return R.fail(ApiResultEnum.ERROR_MONTH_NOT_SUPPORT);
    }

    @ExceptionHandler(ApiException.class)
    public R ApiException(ApiException ex) {
        logger.error(ex.getMessage(),ex);
        return R.fail(ex.getStatus(),ex.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public R RuntimeException(RuntimeException ex){
        logger.error(ex.getMessage(),ex);
        return R.fail(ApiResultEnum.ERROR_RUNTIME);
    }

    @ExceptionHandler(Exception.class)
    public R exception(Exception ex){
        logger.error(ex.getMessage(),ex);
        return R.fail(ApiResultEnum.ERROR);
    }

}

package top.lrshuai.playwright.exception;

import lombok.extern.slf4j.Slf4j;
import org.opentest4j.AssertionFailedError;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.lrshuai.playwright.base.ApiResultEnum;
import top.lrshuai.playwright.base.R;

import java.io.IOException;

/**
 * 全局异常捕获
 * @author rstyro
 *
 */
@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

	@ExceptionHandler(NullPointerException.class)
	public R NullPointer(NullPointerException ex){
		log.error(ex.getMessage(),ex);
		return R.error(ApiResultEnum.ERROR_NULL);
	}

	@ExceptionHandler(ClassCastException.class)
	public R ClassCastException(ClassCastException ex){
		log.error(ex.getMessage(),ex);
		return R.error(ApiResultEnum.ERROR_CLASS_CAST);
	}

	@ExceptionHandler(IOException.class)
	public R IOException(IOException ex){
		log.error(ex.getMessage(),ex);
		return R.error(ApiResultEnum.ERROR_IO);
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public R httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex){
		log.error(ex.getMessage(),ex);
		return R.error(ApiResultEnum.ERROR_REQUEST_ERR);
	}

	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	public R httpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex){
		log.error(ex.getMessage(),ex);
		return R.error(ApiResultEnum.ERROR_REQUEST_ERR);
	}

	@ExceptionHandler(ServiceException.class)
	public R ApiException(ServiceException ex) {
		log.error(ex.getMessage(),ex);
		return R.error(ex.getCode(),ex.getMessage());
	}

	@ExceptionHandler(RuntimeException.class)
	public R RuntimeException(RuntimeException ex){
		log.error(ex.getMessage(),ex);
		return R.error(ApiResultEnum.ERROR_RUN);
	}


	@ExceptionHandler(MethodArgumentNotValidException.class)
	public R notPermission(MethodArgumentNotValidException ex){
		String message = ex.getBindingResult().getFieldError().getDefaultMessage();
		log.error(ex.getMessage(),ex);
		return R.error(message);
	}

	/**
	 * 自定义验证异常
	 */
	@ExceptionHandler(BindException.class)
	public R handleBindException(BindException e) {
		log.error(e.getMessage(), e);
		String message = e.getAllErrors().get(0).getDefaultMessage();
		return R.error(message);
	}

	@ExceptionHandler(Exception.class)
	public R exception(Exception ex){
		log.error(ex.getMessage(),ex);
		return R.error(ApiResultEnum.ERROR);
	}


}

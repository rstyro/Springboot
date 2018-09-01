package top.lrshuai.SpringBootmultisource.exception;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import top.lrshuai.SpringBootmultisource.entity.ApiResultEnum;
import top.lrshuai.SpringBootmultisource.entity.RestBody;

/**
 * 全局异常捕获
 * @author Administrator
 *
 */
@RestControllerAdvice
public class RestExceptionHandler {

	private Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);
	
	@ExceptionHandler(Exception.class)  
	public RestBody exceptionHandler(Exception ex) {
		logger.info(ex.getMessage(), ex);
		if(ex instanceof ApiException){
			System.out.println("1");
			ApiException e= (ApiException) ex;
			return new RestBody(e.getStatus(),e.getMessage(), null);
		}else if(ex instanceof NullPointerException){
			System.out.println("3");
			return new RestBody(ApiResultEnum.ERROR_NULL, null);
		}else if(ex instanceof ClassCastException){
			System.out.println("4");
			return new RestBody(ApiResultEnum.ERROR_CLASS_CAST, null);
		}else if(ex instanceof IOException){
			System.out.println("5");
			return new RestBody(ApiResultEnum.ERROR_IO, null);
		}else if(ex instanceof HttpRequestMethodNotSupportedException){
			System.out.println("6");
			return new RestBody(ApiResultEnum.ERROR_MOTHODNOTSUPPORT, null);
		}else if(ex instanceof RuntimeException){
			System.out.println("2");
			return new RestBody(ApiResultEnum.ERROR_RUNTION, null);
		}
		System.out.println("7");
		return new RestBody(ApiResultEnum.ERROR,null);
	} 
	
}

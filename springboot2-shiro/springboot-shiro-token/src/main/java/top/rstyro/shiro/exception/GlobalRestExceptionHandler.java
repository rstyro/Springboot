package top.rstyro.shiro.exception;

import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.rstyro.shiro.commons.Result;

/**
 * 全局异常捕获
 * @author rstyro
 *
 */
@RestControllerAdvice
public class GlobalRestExceptionHandler {

	private Logger logger = LoggerFactory.getLogger(GlobalRestExceptionHandler.class);

	@ExceptionHandler(AuthorizationException.class)
	public Result authorization(AuthorizationException ex){
		logger.error(ex.getMessage(),ex);
		return Result.error("您没有权限访问");
	}

	@ExceptionHandler(Exception.class)
	public Result exception(Exception ex){
		logger.error(ex.getMessage(),ex);
		return Result.error();
	}

}

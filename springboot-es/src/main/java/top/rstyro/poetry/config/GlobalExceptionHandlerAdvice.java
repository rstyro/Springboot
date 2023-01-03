package top.rstyro.poetry.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.rstyro.poetry.commons.ApiException;
import top.rstyro.poetry.commons.R;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

/**
 * 描述：全局统一异常处理
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandlerAdvice {

    /**
     * 参数验证异常
     */
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public R handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String errorMsg =  e.getBindingResult().getFieldError().getDefaultMessage();
        if (StringUtils.hasLength(errorMsg)) {
            return R.fail(errorMsg);
        }
        log.error(e.getMessage(),e);
        return R.fail(e.getMessage());
    }

    /**
     * 参数验证异常
     */
    @ExceptionHandler(value = {ValidationException.class})
    public R constraintViolationException(ValidationException e) {
        if(e instanceof ConstraintViolationException){
            ConstraintViolationException err = (ConstraintViolationException) e;
            ConstraintViolation<?> constraintViolation = err.getConstraintViolations().stream().findFirst().get();
            String messageTemplate = constraintViolation.getMessageTemplate();
            if(!StringUtils.isEmpty(messageTemplate)){
                return R.fail(messageTemplate);
            }
        }
        log.error(e.getMessage(),e);
        return R.fail(e.getMessage());
    }

    @ExceptionHandler(ApiException.class)
    public R ApiException(ApiException ex) {
        log.error(ex.getMessage(),ex);
        return R.fail(ex.getStatus(),ex.getMessage());
    }


    /**
     * 默认异常
     */
    @ExceptionHandler(value = Exception.class)
    public R defaultException(Exception e) {
        log.error("系统异常:" + e.getMessage(), e);
        return R.fail(e.getMessage());
    }

}

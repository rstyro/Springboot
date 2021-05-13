package top.lrshuai.validator.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import top.lrshuai.validator.commons.Result;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.xml.bind.ValidationException;

/**
 * 描述：全局统一异常处理

 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandlerAdvice {

    /**
     * 验证异常
     */
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    @ResponseBody
    public Result handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        String errorMsg = "";
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errorMsg += fieldError.getDefaultMessage() + ";";
        }
        if (!StringUtils.isEmpty(errorMsg)) {
            log.error("MethodArgumentNotValidException：" + errorMsg);
            return Result.error(errorMsg);
        }
        return Result.error();
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result handlerException(Exception e) {
        log.error("系统异常:" + e.getMessage(), e);
        return Result.error();
    }

}

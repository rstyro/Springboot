package top.lrshuai.i18n.intercept;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import org.springframework.web.servlet.support.RequestContext;

import top.lrshuai.i18n.dto.ResponseDTO;
									
@ControllerAdvice(basePackages={"top.lrshuai.i18n.controller"})
public class I18nResponseBodyAdvice implements ResponseBodyAdvice<Object>{

	protected Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public Object beforeBodyWrite(Object obj, MethodParameter method,
			MediaType type, Class<? extends HttpMessageConverter<?>> converter,
			ServerHttpRequest request, ServerHttpResponse response) {
		try {
			if(obj instanceof ResponseDTO){
				ResponseDTO result = (ResponseDTO) obj;
				String resultCode = result.getResultCode();
				if(resultCode!= null){
					HttpServletRequest req = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
					String i18nMsg = getMessage(req, resultCode);
					result.setResultMsg(i18nMsg);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("返回值国际化拦截异常",e);
		}
		return obj;
	}

	@Override
	public boolean supports(MethodParameter arg0,
			Class<? extends HttpMessageConverter<?>> arg1) {
		return true;
	}
	
	/**
	 * 返回国际化的值
	 * @param request
	 * @param key
	 * @return
	 */
	public String getMessage(HttpServletRequest request, String key){
        RequestContext requestContext = new RequestContext(request);
        String value = requestContext.getMessage(key);
        return value;
    }

}

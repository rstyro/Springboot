package top.lrshuai.fli.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
/**
 * 可以验证用户是否登录来拦截，没登陆返回false
 * @author tyro
 *
 */
public class LoginInterceptor implements HandlerInterceptor{

	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		System.out.println("LoginInterceptor 在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作）");
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		System.out.println("LoginInterceptor  请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）");
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
		System.out.println("LoginInterceptor 在请求处理之前进行调用（Controller方法调用之前） 这里是拦截的操作");
		String user = (String) request.getSession().getAttribute("user");
		System.out.println("user="+user);
		if(null == user || "".equals(user)){
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().println("你没登陆，请去登录页面：<a href='http://localhost:8080/login'>登录</a>。");
			return false;
		}
		return true;
	}

}

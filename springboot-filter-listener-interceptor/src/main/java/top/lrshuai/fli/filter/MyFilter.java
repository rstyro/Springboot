package top.lrshuai.fli.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;


/**
 * 
 * 使用注解标注过滤器
 * @WebFilter将一个实现了javax.servlet.Filter接口的类定义为过滤器
 * 属性filterName 声明过滤器的名称,可选
 * 属性urlPatterns指定要过滤 的URL模式,这是一个数组参数，可以指定多个。也可使用属性value来声明.(指定要过滤的URL模式是必选属性)
 */
@WebFilter(filterName="myFilter",urlPatterns={"/*"})
public class MyFilter implements Filter{

	@Override
	public void destroy() {
		System.out.println("myfilter 的 销毁方法");
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain chain)
			throws IOException, ServletException {
		System.out.println("myfilter 的 过滤方法。这里可以执行过滤操作");
		//继续下一个拦截器
		chain.doFilter(arg0, arg1);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		System.out.println("myfilter 的 初始化方法");
	}

}

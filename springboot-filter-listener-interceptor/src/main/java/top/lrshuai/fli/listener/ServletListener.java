package top.lrshuai.fli.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ServletListener implements ServletContextListener{

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		System.out.println("servlet 销毁时调用");
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		System.out.println("servlet 初始化时调用");
	}

}

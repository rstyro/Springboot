package top.lrshuai.fli.listener;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class SessionListener implements HttpSessionListener{

	@Override
	public void sessionCreated(HttpSessionEvent arg0) {
		System.out.println("监听 创建session");
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent arg0) {
		System.out.println("监听 销毁session");
	}

}

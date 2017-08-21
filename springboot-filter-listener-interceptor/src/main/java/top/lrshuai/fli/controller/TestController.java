package top.lrshuai.fli.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RestController
public class TestController {

	@RequestMapping("/test")
	public String test(){
		System.out.println("这是test请求里面的方法");
		return "this is test page";
	}
	
	@RequestMapping("/login")
	public String login(){
		System.out.println("这是在login方法里创建一个session试试");
		((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
		.getRequest().getSession().setAttribute("user", "auth");
		
		return "login success";
	}
	
	@RequestMapping("/logout")
	public String logout(){
		System.out.println("这是在logout方法里销毁session试试");
		((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
		.getRequest().getSession().removeAttribute("user");
		((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
		.getRequest().getSession().invalidate();
		
		return "logout success";
	}
}

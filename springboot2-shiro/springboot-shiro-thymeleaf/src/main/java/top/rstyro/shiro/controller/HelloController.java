package top.rstyro.shiro.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {

	@RequestMapping(value={"/index","/"})
	public String index(Model model){
		model.addAttribute("title", "测试");
		model.addAttribute("atext", "小鹿乱撞");
		return "index";
	}

	@RequestMapping("/toLogin")
	public String toLogin(Model model){
		return "login";
	}

	@RequestMapping("/login")
	public String login(Model model,String username,String password){
		Subject subject = SecurityUtils.getSubject();
		try{
			// 打印是否授权
			System.out.println("是否授权："+subject.isAuthenticated());
			// 用户登陆认证
			subject.login(new UsernamePasswordToken(username,password));
			// true 登陆之后就是授权
			System.out.println("是否授权："+subject.isAuthenticated());
			return "redirect:/index";
		}catch (UnknownAccountException e){
			model.addAttribute("msg", "用户不存在");
			System.out.println("用户不存在");
		}catch (IncorrectCredentialsException e){
			model.addAttribute("msg", "密码错误");
			System.out.println("密码错误");
		}catch (AuthenticationException e){
			model.addAttribute("msg", "认证失败");
			System.out.println("认证失败");
			e.printStackTrace();
		}
		return "login";
	}


	@RequestMapping("logout")
	public String logout(){
		SecurityUtils.getSubject().logout();
		return "login";
	}

	@RequestMapping("/noauth")
	@ResponseBody
	public String noauth(){
		return "没有权限访问";
	}

	@RequestMapping("/user/add")
	public String add(){
		return "user/add";
	}

	@RequestMapping("/user/edit")
	public String edit(){
		return "user/edit";
	}

	@RequestMapping("/user/del")
	public String del(){
		return "user/del";
	}

	/**
	 * 注解是否有test角色
	 * @return
	 */
	@RequiresRoles("test")
	@RequestMapping("/test1")
	public String test1(){
		return "test";
	}

	/**
	 * 是否拥有 rstyro:test:query 权限
	 * @return
	 */
	@RequestMapping("/test2")
	@RequiresPermissions("rstyro:test:query")
	public String test2(){
		return "test";
	}

}

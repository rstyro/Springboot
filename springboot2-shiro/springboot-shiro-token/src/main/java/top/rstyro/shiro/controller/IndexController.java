package top.rstyro.shiro.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.rstyro.shiro.commons.Consts;
import top.rstyro.shiro.commons.Result;
import top.rstyro.shiro.shiro.CustomerToken;
import top.rstyro.shiro.shiro.uitls.ShiroUtils;
import top.rstyro.shiro.sys.entity.User;
import top.rstyro.shiro.sys.service.IUserService;
import top.rstyro.shiro.utils.IdUtils;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
public class IndexController {

	@Autowired
	private IUserService userService;


//	@Autowired
//	private ITestService testService;

	@Autowired
	private RedisTemplate<String,Object> redisTemplate;

	@RequestMapping("/")
	public Result index(){
		return Result.ok("接口调用，前后端分离项目shiro");
	}

	@RequestMapping("/login")
	public Result login(String username, String password){
		Subject subject = SecurityUtils.getSubject();
		try{
			User currentUser = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
			if(ObjectUtils.isEmpty(currentUser)){
				return Result.error("用户不存在");
			}
			// 密码校验...讲道理肯定是各种加密的
			if(!currentUser.getPassword().equals(password)){
				return Result.error("用户名或密码错误");
			}
			// 各种校验通过之后，进行shiro登陆认证
//			String token = IdUtils.simpleUUID();
			Session session = subject.getSession();
			String newToken = session.getId().toString();
			String oldUserToken = ShiroUtils.getOldUserToken(currentUser.getId());
			System.out.println("oldTOken="+oldUserToken);
			System.out.println("newToken="+newToken);
			session.setAttribute(Consts.OLD_TOKEN,oldUserToken);
			ShiroUtils.setLoginInfo(currentUser,newToken);
			// shiro 认证
			subject.login(new CustomerToken(newToken));
			// session ID 当作token
			String sessionId = subject.getSession().getId().toString();
			currentUser.setToken(sessionId);
			// 暴力直接返回用户信息，真实肯定不是这样干的，密码啥的各种敏感信息是不返回的
			return Result.ok(currentUser);
		}catch (UnknownAccountException e){
			log.error("用户不存在",e);
		}catch (IncorrectCredentialsException e){
			log.error("密码错误",e);
		}catch (AuthenticationException e){
			log.error("认证失败",e);
		}
		return  Result.error();
	}


	// 下面就是测试接口权限
	@RequestMapping("/user/list")
	@RequiresPermissions("user:list")
	public Result list(){
		System.out.println("user/list....");
		return Result.ok("user/list-data");
	}

	@RequestMapping("/user/add")
	@RequiresPermissions("user:add")
	public Result add(){
		System.out.println("user/add....");
		return Result.ok("user/add-data");
	}

	@RequestMapping("/user/edit")
	@RequiresPermissions("user:edit")
	public Result edit(){
		System.out.println("user/edit....");
		return Result.ok("user/edit-data");
	}

	@RequestMapping("/user/del")
	@RequiresPermissions("user:del")
	public Result del(){
		System.out.println("user/del....");
		return Result.ok("user/del-data");
	}

	@RequestMapping("/test")
	@RequiresPermissions("user:del")
	public Result test(){
		System.out.println("test...");
		userService.testTransactional();
		return Result.ok();
	}

//	@RequestMapping("/test2")
//	@RequiresPermissions("user:del")
//	public Result test2(){
//		System.out.println("test2...");
//		testService.testTransactional();
//		return Result.ok();
//	}


}

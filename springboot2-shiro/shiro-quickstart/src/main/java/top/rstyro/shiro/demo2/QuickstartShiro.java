package top.rstyro.shiro.demo2;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuickstartShiro {
    private static Logger log = LoggerFactory.getLogger(QuickstartShiro.class);
    public static void main(String[] args) {
        // 创建默认的安全管理器
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        // 通过.ini文件创建给安全管理器设置默认的Realm
        securityManager.setRealm(new CustomerRealm());
        // 全局安全工具类，设置安全管理器进行认证
        SecurityUtils.setSecurityManager(securityManager);
        // 创建令牌
        UsernamePasswordToken token = new UsernamePasswordToken("rstyro", "123456");
        // 得到当前的 subject，也就是当前的用户
        Subject subject = SecurityUtils.getSubject();
        try{
            // 打印是否授权
            System.out.println("是否授权："+subject.isAuthenticated());
            // 用户登陆认证
            subject.login(token);
            // true 登陆之后就是授权
            System.out.println("是否授权："+subject.isAuthenticated());
        }catch (UnknownAccountException e){
            System.out.println("用户不存在");
            e.printStackTrace();
        }catch (IncorrectCredentialsException e){
            System.out.println("密码错误");
            e.printStackTrace();
        }catch (AuthenticationException e){
            System.out.println("认证失败");
            e.printStackTrace();
        }

    }
}

package top.rstyro.shiro.demo2;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * 自定义 realm
 */
public class CustomerRealm  extends AuthorizingRealm {

    /**
     * 授权
     * @param principals
     * @return
     */
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println(principals.getPrimaryPrincipal());
        return null;
    }

    /**
     * 认证
     * @param token 令牌
     * @return
     * @throws AuthenticationException
     */
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //在subject.login的时候传过来的token
        //在token中获取 用户名
        String principal = (String) token.getPrincipal();
        System.out.println("认证的用户名="+principal);

        //实际开发中应当,查询数据库。这里假设username,password是从数据库获得的信息
        String username="rstyro";
        String password="123456";
        // 匹配是否存在这个用户
        if(username.equals(principal)){
            //参数1:可以是用户对象也可以是用户名，在上面doGetAuthorizationInfo()授权方法需要用到
            //参数2:返回数据库中正确密码
            //参数3:提供当前realm的名字 this.getName();
            SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(principal,password,this.getName());
            return simpleAuthenticationInfo;
        }
        return null;
    }
}

package top.rstyro.shiro.shiro;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import top.rstyro.shiro.sys.entity.Menu;
import top.rstyro.shiro.sys.entity.Role;
import top.rstyro.shiro.sys.entity.User;
import top.rstyro.shiro.sys.service.IRoleMenuService;
import top.rstyro.shiro.sys.service.IUserRoleService;
import top.rstyro.shiro.sys.service.IUserService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 自定义 realm
 */
public class CustomerRealm extends AuthorizingRealm {


    // 不能@Autowired这样注入，这样会导致事务不生效的。可以通过ApplicationContextUtils.getBean("beanName") 获取
    // 这里不改了，可以测试事务不会滚的接口：/sys/user-role/test
    @Autowired
    private IUserService userService;

    @Autowired
    private IUserRoleService userRoleService;

    @Autowired
    private IRoleMenuService roleMenuService;

    /**
     * 授权
     * @param principals
     * @return
     */
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("授权："+principals.getPrimaryPrincipal());
        // 下面认证时第一个参数传过来的
        User user = (User) principals.getPrimaryPrincipal();
        List<Role> roles = userRoleService.getRoleByUserId(user.getId());
        if(!ObjectUtils.isEmpty(roles)){
            SimpleAuthorizationInfo simpleAuthenticationInfo = new SimpleAuthorizationInfo();
            // 给用户添加角色
            roles.forEach(r->{
                simpleAuthenticationInfo.addRole(r.getRoleName());
            });
//            menuService.list(new LambdaQueryWrapper<Menu>().in(get));
            System.out.println("角色列表："+roles.toString());
            List<Menu> menuByRoleIds = roleMenuService.getMenuByRoleIds(roles.stream().map(Role::getId).collect(Collectors.toList()));
            System.out.println("总菜单对象列表："+menuByRoleIds.toString());
            List<String> permissions = menuByRoleIds.stream().map(Menu::getPermissions).collect(Collectors.toList());
            System.out.println("总菜单列表："+permissions.toString());
            // 添加菜单权限
            simpleAuthenticationInfo.addStringPermissions(permissions);
            return simpleAuthenticationInfo;
        }
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
        User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, principal));
        if(user!=null){
            // 密码放入数据库的密码 和 登陆传上来的比较（shiro帮我们处理）
            SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(user,user.getPassword(),this.getName());
            return simpleAuthenticationInfo;
        }
        // 返回null 则会报 UnknownAccountException 异常
        return null;
    }
}

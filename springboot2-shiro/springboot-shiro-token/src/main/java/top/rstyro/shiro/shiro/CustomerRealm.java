package top.rstyro.shiro.shiro;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.ObjectUtils;
import top.rstyro.shiro.commons.Consts;
import top.rstyro.shiro.shiro.uitls.ShiroUtils;
import top.rstyro.shiro.sys.entity.Menu;
import top.rstyro.shiro.sys.entity.Role;
import top.rstyro.shiro.sys.entity.User;
import top.rstyro.shiro.sys.service.IRoleMenuService;
import top.rstyro.shiro.sys.service.IUserRoleService;
import top.rstyro.shiro.sys.service.IUserService;
import top.rstyro.shiro.utils.ApplicationContextUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 自定义 realm
 */
public class CustomerRealm extends AuthorizingRealm {

    // 下面这种方式 注入 使用mybatis-plus 事务是不生效的。
    // 这样注入是没有代理器的，而事务是通过代理
    // 它会比spring一般的初始化实例要早，而spring的实例都是单例的，也就是上面的spring 还没来得及把前后处理事务的代码注入进去，shiro初始化就把这两个类的代理对象生成了
    // 可参考文章：https://www.guitu18.com/post/2019/10/30/56.html
//    @Autowired
//    private IUserService userService;
//
//    @Autowired
//    private IUserRoleService userRoleService;
//
//    @Autowired
//    private IRoleMenuService roleMenuService;

    /**
     * 支持的 AuthenticationToken 类型
     * @param token
     * @return
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof CustomerToken;
    }

    /**
     * 授权
     * @param principals
     * @return
     */
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("授权："+principals.getPrimaryPrincipal());
        // 下面认证时第一个参数传过来的
        User user = (User) principals.getPrimaryPrincipal();
        IUserRoleService userRoleService=  ApplicationContextUtils.getBean(IUserRoleService.class);
        IRoleMenuService roleMenuService= ApplicationContextUtils.getBean(IRoleMenuService.class);
        List<Role> roles = userRoleService.getRoleByUserId(user.getId());
//        Long id = (Long) principals.getPrimaryPrincipal();
//        List<Role> roles = userRoleService.getRoleByUserId(id);
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
        String loginToken = (String) token.getPrincipal();
        RedisTemplate redisTemplate=  ApplicationContextUtils.getBean("redisTemplate",RedisTemplate.class);
        Object obj = redisTemplate.opsForValue().get(Consts.REDIS_TOKEN_KEY_PREFIX + loginToken);
        User user = JSON.parseObject(obj.toString(),User.class);
        if(user!=null){
            // 密码放入数据库的密码 和 登陆传上来的比较（shiro帮我们处理）
            SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(user,token.getCredentials(),this.getName());
            //清除当前主体旧的会话，相当于你在新电脑上登录系统，把你之前在旧电脑上登录的会话挤下去
            ShiroUtils.deleteCache(user.getUsername(),true);
            return simpleAuthenticationInfo;
        }
        // 返回null 则会报 UnknownAccountException 异常
        return null;
    }

    /**
     * 如果是admin 用户直接通过，不走权限限制
     */
    @Override
    public boolean isPermitted(PrincipalCollection principals, String permission) {
        User user = (User)principals.getPrimaryPrincipal();
        return "admin".equals(user.getUsername()) || super.isPermitted(principals, permission);
    }

    /**
     * 如果是admin 用户直接通过，不走权限限制
     */
    @Override
    public boolean hasRole(PrincipalCollection principals, String roleIdentifier) {
        User user = (User)principals.getPrimaryPrincipal();
        return "admin".equals(user.getUsername()) || super.hasRole(principals, roleIdentifier);
    }

    /**
     * 清空已经放入缓存的认证信息。
     * */
    @Override
    protected void clearCache(PrincipalCollection principals) {
        System.out.println("自定义 realm clearCache");
        System.out.println("principals="+JSON.toJSONString(principals));
        System.out.println("principals="+JSON.toJSONString(principals));
        ShiroRedisCache cache = (ShiroRedisCache) this.getCacheManager().getCache(Consts.SHIRO_AUTHENTICATION_CACHE);
        System.out.println("cache="+JSON.toJSONString(cache.size()));
        System.out.println("cache="+JSON.toJSONString(cache.keys()));
        System.out.println("cache="+JSON.toJSONString(cache.values()));
        User user = (User) principals.getPrimaryPrincipal();
        // 清除旧的认证缓存
        if(cache != null&& cache.size()>0){
            Iterator<String> iterator = cache.keys().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                if(user.getToken().equals(key)){
                    cache.remove(key);
                    break;
                }
            }
        }

        this.getCacheManager().getCache(Consts.SHIRO_AUTHORIZATION_CACHE).remove(principals);

        super.clearCache(principals);
    }
}

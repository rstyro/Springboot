package top.rstyro.shiro.sys.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import top.rstyro.shiro.sys.entity.Role;
import top.rstyro.shiro.sys.entity.User;
import top.rstyro.shiro.sys.entity.UserRole;
import top.rstyro.shiro.sys.mapper.UserRoleMapper;
import top.rstyro.shiro.sys.service.IRoleService;
import top.rstyro.shiro.sys.service.IUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.rstyro.shiro.sys.service.IUserService;

import java.util.List;
import java.util.UUID;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author rstyro
 * @since 2021-04-03
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public List<Role> getRoleByUserId(Long userId) {
        return userRoleMapper.getRoleByUserId(userId);
    }

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IUserService userService;

    @Override
    @Transactional
    public void testTransactional() {
        // 测试shior 是否和事务冲突
        roleService.save(new Role().setRoleName(UUID.randomUUID().toString().replaceAll("-","")).setRemark("随便测试"));
        System.out.println("===========");
        System.out.println(1/0);
        userService.save(new User().setNickName("rstyro").setUsername("rstyro").setPassword("abc"));
    }
}

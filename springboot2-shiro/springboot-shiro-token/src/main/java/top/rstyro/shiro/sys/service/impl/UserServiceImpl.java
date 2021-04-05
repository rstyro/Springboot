package top.rstyro.shiro.sys.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import top.rstyro.shiro.sys.entity.Role;
import top.rstyro.shiro.sys.entity.User;
import top.rstyro.shiro.sys.mapper.UserMapper;
import top.rstyro.shiro.sys.service.IRoleService;
import top.rstyro.shiro.sys.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.rstyro.shiro.utils.IdUtils;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author rstyro
 * @since 2021-04-03
 */
@Service
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private IRoleService roleService;

    @Override
    @Transactional
    public void testTransactional() {
        // 测试shior 是否和事务冲突
        roleService.save(new Role().setRoleName(IdUtils.randomUUID()).setRemark("随便测试"));
        System.out.println("===========");
        System.out.println(1/0);
        this.save(new User().setNickName("rstyro").setUsername("rstyro").setPassword("abc"));
    }
}

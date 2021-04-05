package top.rstyro.shiro.sys.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import top.rstyro.shiro.sys.entity.Role;
import top.rstyro.shiro.sys.entity.UserRole;
import top.rstyro.shiro.sys.mapper.UserRoleMapper;
import top.rstyro.shiro.sys.service.IUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

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
}

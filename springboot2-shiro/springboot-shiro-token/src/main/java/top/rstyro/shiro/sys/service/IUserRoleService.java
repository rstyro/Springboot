package top.rstyro.shiro.sys.service;

import top.rstyro.shiro.sys.entity.Role;
import top.rstyro.shiro.sys.entity.UserRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author rstyro
 * @since 2021-04-03
 */
public interface IUserRoleService extends IService<UserRole> {
    public List<Role> getRoleByUserId(Long userId);
}

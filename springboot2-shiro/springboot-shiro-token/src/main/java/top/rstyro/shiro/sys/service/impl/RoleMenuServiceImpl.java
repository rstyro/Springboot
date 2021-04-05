package top.rstyro.shiro.sys.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import top.rstyro.shiro.sys.entity.Menu;
import top.rstyro.shiro.sys.entity.RoleMenu;
import top.rstyro.shiro.sys.mapper.RoleMenuMapper;
import top.rstyro.shiro.sys.service.IRoleMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 角色菜单关系 服务实现类
 * </p>
 *
 * @author rstyro
 * @since 2021-04-03
 */
@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements IRoleMenuService {


    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Override
    public List<Menu> getMenuByRoleIds(Collection<Long> roleIds) {
        return roleMenuMapper.getMenuByRoleIds(roleIds);
    }
}

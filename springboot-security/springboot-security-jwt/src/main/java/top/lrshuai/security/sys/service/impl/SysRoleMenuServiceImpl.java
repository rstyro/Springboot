package top.lrshuai.security.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import top.lrshuai.security.sys.entity.SysMenu;
import top.lrshuai.security.sys.entity.SysRoleMenu;
import top.lrshuai.security.sys.mapper.SysRoleMenuMapper;
import top.lrshuai.security.sys.service.ISysMenuService;
import top.lrshuai.security.sys.service.ISysRoleMenuService;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色菜单关系 服务实现类
 * </p>
 *
 * @author rstyro
 * @since 2021-07-22
 */
@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements ISysRoleMenuService {

    private ISysMenuService sysMenuService;

    @Autowired
    public void setSysMenuService(ISysMenuService sysMenuService) {
        this.sysMenuService = sysMenuService;
    }

    @Override
    public Set<String> getPermissionsByRoleIds(Collection<Long> roleIds) {
        List<SysRoleMenu> list = this.list(new LambdaQueryWrapper<SysRoleMenu>().in(SysRoleMenu::getRoleId, roleIds));
        if(!ObjectUtils.isEmpty(list)){
            Set<Long> menuIds = list.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toSet());
            List<SysMenu> sysMenus = sysMenuService.listByIds(menuIds);
            // 所有菜单资源权限
            return sysMenus.stream().map(SysMenu::getPermissions).collect(Collectors.toSet());
        }
        return new HashSet<>();
    }
}

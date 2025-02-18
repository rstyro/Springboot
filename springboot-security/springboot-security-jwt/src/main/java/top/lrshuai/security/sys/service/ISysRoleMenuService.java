package top.lrshuai.security.sys.service;

import top.lrshuai.security.sys.entity.SysRoleMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 角色菜单关系 服务类
 * </p>
 *
 * @author rstyro
 * @since 2021-07-22
 */
public interface ISysRoleMenuService extends IService<SysRoleMenu> {
    /**
     * 查询所有角色下的所有资源权限
     * @return
     */
    public Set<String> getPermissionsByRoleIds(Collection<Long> roleIds);
}

package top.rstyro.shiro.sys.service;

import org.apache.ibatis.annotations.Param;
import top.rstyro.shiro.sys.entity.Menu;
import top.rstyro.shiro.sys.entity.RoleMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 角色菜单关系 服务类
 * </p>
 *
 * @author rstyro
 * @since 2021-04-03
 */
public interface IRoleMenuService extends IService<RoleMenu> {
    public List<Menu> getMenuByRoleIds(Collection<Long> roleIds);
}

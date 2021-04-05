package top.rstyro.shiro.sys.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import top.rstyro.shiro.sys.entity.Menu;
import top.rstyro.shiro.sys.entity.RoleMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 角色菜单关系 Mapper 接口
 * </p>
 *
 * @author rstyro
 * @since 2021-04-03
 */
@Component
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

    public List<Menu> getMenuByRoleIds(@Param("roleIds") Collection<Long> roleIds);
}

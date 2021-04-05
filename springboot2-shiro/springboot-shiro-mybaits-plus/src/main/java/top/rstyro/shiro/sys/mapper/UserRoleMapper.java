package top.rstyro.shiro.sys.mapper;

import org.springframework.stereotype.Component;
import top.rstyro.shiro.sys.entity.Role;
import top.rstyro.shiro.sys.entity.UserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author rstyro
 * @since 2021-04-03
 */
@Component
public interface UserRoleMapper extends BaseMapper<UserRole> {

    public List<Role> getRoleByUserId(Long userId);
}

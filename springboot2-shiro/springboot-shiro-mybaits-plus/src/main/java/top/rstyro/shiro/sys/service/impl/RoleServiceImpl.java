package top.rstyro.shiro.sys.service.impl;

import top.rstyro.shiro.sys.entity.Role;
import top.rstyro.shiro.sys.mapper.RoleMapper;
import top.rstyro.shiro.sys.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author rstyro
 * @since 2021-04-03
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

}

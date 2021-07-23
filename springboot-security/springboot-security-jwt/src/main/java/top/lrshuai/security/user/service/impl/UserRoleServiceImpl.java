package top.lrshuai.security.user.service.impl;

import top.lrshuai.security.user.entity.UserRole;
import top.lrshuai.security.user.mapper.UserRoleMapper;
import top.lrshuai.security.user.service.IUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author rstyro
 * @since 2021-07-22
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

}

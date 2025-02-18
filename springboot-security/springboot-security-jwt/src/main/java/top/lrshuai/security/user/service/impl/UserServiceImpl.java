package top.lrshuai.security.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.lrshuai.security.user.entity.User;
import top.lrshuai.security.user.mapper.UserMapper;
import top.lrshuai.security.user.service.IUserService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author rstyro
 * @since 2021-07-16
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}

package top.rstyro.shiro.sys.service.impl;

import top.rstyro.shiro.sys.entity.User;
import top.rstyro.shiro.sys.mapper.UserMapper;
import top.rstyro.shiro.sys.service.IUserService;
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
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}

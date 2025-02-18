package top.lrshuai.sqlite.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.lrshuai.sqlite.demo.entity.User;
import top.lrshuai.sqlite.demo.mapper.UserMapper;
import top.lrshuai.sqlite.demo.service.IUserService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author rstyro
 * @since 2022-03-25
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}

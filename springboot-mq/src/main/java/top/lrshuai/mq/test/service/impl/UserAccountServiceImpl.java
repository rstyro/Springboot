package top.lrshuai.mq.test.service.impl;

import top.lrshuai.mq.test.entity.UserAccount;
import top.lrshuai.mq.test.mapper.UserAccountMapper;
import top.lrshuai.mq.test.service.IUserAccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户账户余额表 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2018-11-24
 */
@Service
public class UserAccountServiceImpl extends ServiceImpl<UserAccountMapper, UserAccount> implements IUserAccountService {

}

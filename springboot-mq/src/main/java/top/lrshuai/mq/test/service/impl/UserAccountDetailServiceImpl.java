package top.lrshuai.mq.test.service.impl;

import top.lrshuai.mq.test.entity.UserAccountDetail;
import top.lrshuai.mq.test.mapper.UserAccountDetailMapper;
import top.lrshuai.mq.test.service.IUserAccountDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 活期积分流水表 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2018-11-24
 */
@Service
public class UserAccountDetailServiceImpl extends ServiceImpl<UserAccountDetailMapper, UserAccountDetail> implements IUserAccountDetailService {

}

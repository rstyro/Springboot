package top.lrshuai.mq.test.service;

import top.lrshuai.mq.test.entity.AccountPlan;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 入账计划表 服务类
 * </p>
 *
 * @author jobob
 * @since 2018-11-24
 */
public interface IAccountPlanService extends IService<AccountPlan> {

    public void saveAccountPlan(AccountPlan plan) throws  Exception;
}

package top.lrshuai.mq.test.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import top.lrshuai.mq.test.entity.AccountPlan;
import top.lrshuai.mq.test.service.IAccountPlanService;
import top.lrshuai.mq.test.service.impl.AccountPlanServiceImpl;

import java.util.List;

/**
 * <p>
 * 入账计划表 前端控制器
 * </p>
 *
 * @author jobob
 * @since 2018-11-24
 */
@RestController
@RequestMapping("/mq/plan")
public class AccountPlanController {

    @Autowired
    private IAccountPlanService accountPlanService;

    @GetMapping("/product")
    public Object product(AccountPlan plan) throws Exception {
        accountPlanService.saveAccountPlan(plan);
        return  "success";
    }

}

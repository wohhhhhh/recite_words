package com.feidian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.feidian.domain.entity.ResponseResult;
import com.feidian.domain.entity.UserPlan;


/**
 * (UserPlan)表服务接口
 *
 * @author makejava
 * @since 2023-05-21 15:27:33
 */
public interface UserPlanService extends IService<UserPlan> {

    ResponseResult viewPlanDetail(Integer id);

    ResponseResult makePlan(UserPlan userPlan);
}


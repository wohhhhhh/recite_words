package com.feidian.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feidian.domain.entity.ResponseResult;
import com.feidian.domain.entity.UserPlan;
import com.feidian.domain.entity.Wordbook;
import com.feidian.domain.vo.UserPlanVO;
import com.feidian.enums.AppHttpCodeEnum;
import com.feidian.handler.exception.SystemException;
import com.feidian.mapper.UserPlanMapper;
import com.feidian.service.UserPlanService;
import com.feidian.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

/**
 * (UserPlan)表服务实现类
 *
 * @author makejava
 * @since 2023-05-21 15:27:33
 */
@Service("userPlanService")
public class UserPlanServiceImpl extends ServiceImpl<UserPlanMapper, UserPlan> implements UserPlanService {
    @Autowired
    UserPlanMapper userPlanMapper;

    @Override
    public ResponseResult viewPlanDetail(Integer id) {
            // 查询单词计划信息
            UserPlan userPlan = userPlanMapper.selectById(id);
            //封装成UserPlanVO
            UserPlanVO vo = BeanCopyUtils.copyBean(userPlan, UserPlanVO.class);
            return ResponseResult.okResult(vo);
    }

    @Override
    public ResponseResult makePlan(UserPlan userPlan) {
        //对数据进行非空判断
        if (userPlan.getStartDate() == null) {
            throw new SystemException(AppHttpCodeEnum.STARTDATE_NOT_NULL);
        }
        if (userPlan.getUserId() == null) {
            throw new SystemException(AppHttpCodeEnum.USERID_NOT_NULL);
        }
        if (userPlan.getTotalDays() == null) {
            throw new SystemException(AppHttpCodeEnum.TOTALDAYS_NOT_NULL);
        }
        if (userPlan.getWordsPerDay() == null) {
            throw new SystemException(AppHttpCodeEnum.WORDSPERDAY_NOT_NULL);
        }
        if (userPlan.getWordbookId() == null) {
            throw new SystemException(AppHttpCodeEnum.WORDBOOKID_NOT_NULL);
        }
        userPlan.setFinishedDays(0);
        userPlan.setFinishedWords(0);
        userPlan.setTodayFinishedWords(0);
        userPlan.setInPlanFinishedWords(0);
        userPlan.setWordsToReview(0);
        // 判断开始日期是否符合规范
        if(!IsStartDateStandardized(userPlan.getStartDate())){
            throw new SystemException(AppHttpCodeEnum.STARTDATE_NOT_STANDARDIZED);
        }
        // 计算结束日期
        countEndDate(userPlan);

        //获取当前时间
        Date now = new Date();

        //设置gmt_create和gmt_modified字段值
        userPlan.setGmtCreate(now);
        userPlan.setGmtModified(now);

        //存入数据库
        // 查询userPlan表是否存在userId对应的记录
        QueryWrapper<UserPlan> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userPlan.getUserId());
        UserPlan plan = getOne(queryWrapper);

        if (plan != null) {
            // 记录存在,更新
            userPlan.setId(plan.getId());
            updateById(userPlan);
        } else {
            // 记录不存在,插入
            save(userPlan);
        }
        return ResponseResult.okResult();

    }

    private boolean IsStartDateStandardized(LocalDate startDate) {
        //获取当前日期
        LocalDate now = LocalDate.now();

        //如果startDate大于现在时间,返回true,否则返回false
        if (startDate.isAfter(now)) {
            return true;
        } else {
            return false;
        }
    }

    private void countEndDate(UserPlan userPlan){
        //获取startDate,现在应是LocalDate类型
        LocalDate startDate = userPlan.getStartDate();
        Integer totalDays = userPlan.getTotalDays();

        //使用LocalDate计算endDate
        LocalDate endDate = startDate.plusDays(totalDays);

        //设置endDate,现在也是LocalDate类型
        userPlan.setEndDate(endDate);
    }
}

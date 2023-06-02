package com.feidian.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feidian.domain.dto.MakeUserPlanDTO;
import com.feidian.domain.entity.ResponseResult;
import com.feidian.domain.entity.UserPlan;
import com.feidian.domain.entity.Word;
import com.feidian.domain.entity.Wordbook;
import com.feidian.domain.vo.UserPlanVO;
import com.feidian.enums.AppHttpCodeEnum;
import com.feidian.handler.exception.SystemException;
import com.feidian.mapper.UserPlanMapper;
import com.feidian.mapper.WordMapper;
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

    @Autowired
    WordMapper wordMapper;

    @Override
    public ResponseResult viewPlanDetail(Integer id) {
            // 查询单词计划信息
            UserPlan userPlan = userPlanMapper.selectById(id);
            //封装成UserPlanVO
            UserPlanVO vo = BeanCopyUtils.copyBean(userPlan, UserPlanVO.class);
            return ResponseResult.okResult(vo);
    }

    @Override
    public ResponseResult makePlan(MakeUserPlanDTO makeUserPlanDTO) {
        // 先把数据取出
        LocalDate startDate = makeUserPlanDTO.getStartDate();
        Integer userId = makeUserPlanDTO.getUserId();
        Integer totalDays = makeUserPlanDTO.getTotalDays();
        Integer wordbookId = makeUserPlanDTO.getWordbookId();

        //对数据进行非空判断
        if (startDate == null) {
            throw new SystemException(AppHttpCodeEnum.STARTDATE_NOT_NULL);
        }
        if (userId == null) {
            throw new SystemException(AppHttpCodeEnum.USERID_NOT_NULL);
        }
        if (totalDays == null) {
            throw new SystemException(AppHttpCodeEnum.TOTALDAYS_NOT_NULL);
        }
        if (wordbookId == null) {
            throw new SystemException(AppHttpCodeEnum.WORDBOOKID_NOT_NULL);
        }
        UserPlan userPlan=new UserPlan();
        userPlan.setUserId(userId);
        userPlan.setStartDate(startDate);
        userPlan.setTotalDays(totalDays);
        userPlan.setWordbookId(wordbookId);

        //TODO 根据所选的单词书id，和持续日期

        // 判断开始日期是否符合规范
        if(!IsStartDateStandardized(makeUserPlanDTO.getStartDate())){
            throw new SystemException(AppHttpCodeEnum.STARTDATE_NOT_STANDARDIZED);
        }
        // 计算结束日期
        LocalDate endDate = countEndDate(makeUserPlanDTO);
        userPlan.setEndDate(endDate);

        //获取当前时间
        Date now = new Date();

        //设置gmt_create和gmt_modified字段值
        userPlan.setGmtCreate(now);
        userPlan.setGmtModified(now);

        // 有些默认值为0的字段
        Integer wordsPerDays=countWordsPerDay(wordbookId,totalDays);
        userPlan.setWordsPerDay(wordsPerDays);
        userPlan.setFinishedDays(0);
        userPlan.setFinishedWords(0);
        userPlan.setInPlanFinishedWords(0);
        userPlan.setTodayFinishedWords(0);
        userPlan.setWordsToReview(0);
        //存入数据库
        // 查询userPlan表是否存在userId对应的记录
        QueryWrapper<UserPlan> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", makeUserPlanDTO.getUserId());
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

    // 开始日期怎么说不能小于今天吧
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

    private LocalDate countEndDate(MakeUserPlanDTO makeUserPlanDTO){
        //获取startDate,现在应是LocalDate类型
        LocalDate startDate = makeUserPlanDTO.getStartDate();
        Integer totalDays = makeUserPlanDTO.getTotalDays();

        //使用LocalDate计算endDate
        LocalDate endDate = startDate.plusDays(totalDays);

        return endDate;
    }

    private Integer countWordsPerDay(Integer wordbookId, Integer totalDays) {
        // 1. 查询该单词本中的所有单词
        LambdaQueryWrapper<Word> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Word::getWordbookId,wordbookId);
        List<Word> words = wordMapper.selectList(lambdaQueryWrapper);

        // 2. 统计单词总数
        int totalCount = words.size();

        // 3. 计算每天需要背的单词数
        int countPerDay = totalCount / totalDays;

        // 4. 最后一天需要背的单词数可能少一些,处理这种情况
        int lastDayCount = totalCount % totalDays;
        if (lastDayCount > 0) {
            countPerDay++;
        }

        return countPerDay;
    }
}

package com.feidian.domain.entity;

import java.time.LocalDate;
import java.util.Date;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (UserPlan)表实体类
 *
 * @author makejava
 * @since 2023-05-21 15:27:32
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_plan")
public class UserPlan  {
    @TableId
    private Integer id;

    private Integer userId;

    private Integer wordbookId;
    
    private LocalDate startDate;
    
    private LocalDate endDate;
    
    private Integer totalDays;
    
    private Integer wordsPerDay;
    
    private Integer finishedDays;
    
    private Integer finishedWords;
    
    private Integer todayFinishedWords;
    
    private Integer inPlanFinishedWords;
    
    private Integer wordsToReview;
    
    private Date gmtCreate;
    
    private Date gmtModified;

}


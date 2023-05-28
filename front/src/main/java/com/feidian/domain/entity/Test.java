package com.feidian.domain.entity;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * (Test)表实体类
 *
 * @author makejava
 * @since 2023-05-22 17:33:53
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("test")
public class Test  {
    @TableId
    private Integer id;

    private Integer userId;

    private Integer wordbookId;
    
    private String wordIds;

    private LocalDate startTime;
    
    private LocalDate endTime;
    
    private String testDuration;
    
    private Integer accuracy;
    
    private Integer testNumber;

    private Date gmtCreate;

    private Date gmtModified;
}


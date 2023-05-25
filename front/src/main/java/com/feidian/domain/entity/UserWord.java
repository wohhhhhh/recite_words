package com.feidian.domain.entity;

import java.util.Date;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (UserWord)表实体类
 *
 * @author makejava
 * @since 2023-05-22 19:06:02
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_word")
public class UserWord  {
    @TableId
    private Integer id;

    private Integer userId;
    
    private Integer wordId;
    // 不记得为0，记得为1
    private Integer isFamiliar;

    private Integer reviewTimesFamiliar;
    
    private Integer reviewTimesVague;
    
    private Date gmtCreate;
    
    private Date gmtModified;
}


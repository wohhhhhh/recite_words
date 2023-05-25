package com.feidian.domain.entity;

import java.util.Date;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (Word)表实体类
 *
 * @author makejava
 * @since 2023-05-20 19:55:21
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("word")
public class Word  {
    @TableId
    private Integer id;
    
    private Integer wordbookId;
    
    private String value;
    
    private String meaningChinese;
    
    private String example;
    
    private Date gmtCreate;
    
    private Date gmtModified;
}


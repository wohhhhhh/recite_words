package com.feidian.domain.entity;

import java.util.Date;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (Wordbook)表实体类
 *
 * @author makejava
 * @since 2023-05-20 16:07:09
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("wordbook")
public class Wordbook  {
    @TableId
    private Long id;
    private String name;
    
    private String intro;
    
    private String suitableUsers;
    
    private Date gmtCreate;
    
    private Date gmtModified;

}


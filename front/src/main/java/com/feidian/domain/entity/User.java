package com.feidian.domain.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (User)表实体类
 *
 * @author makejava
 * @since 2023-05-19 14:58:42
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user")
public class User  {
    @TableId
    private Integer id;

    private String userName;
    
    private String password;
    
    private String email;

    private String phone;
    
    private Integer isAdmin;

    private Integer gender;
    
    private Date gmtCreate;
    
    private Date gmtModified;

    private Integer memoryCount;

}


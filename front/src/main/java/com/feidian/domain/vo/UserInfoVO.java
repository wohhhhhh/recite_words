package com.feidian.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserInfoVO {
    /**
     * 主键
     */
    private Integer id;
    private String userName;
    private String password;
    private String email;
    private Integer isAdmin;
    private String phone;
    private Integer gender;


}
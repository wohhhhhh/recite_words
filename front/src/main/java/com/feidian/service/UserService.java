package com.feidian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.feidian.domain.entity.ResponseResult;
import com.feidian.domain.entity.User;

public interface UserService extends IService<User> {


    ResponseResult userInfo();

    ResponseResult updateUserInfo(User user);

    ResponseResult register(User user);
}

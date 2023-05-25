package com.feidian.service;

import com.feidian.domain.entity.ResponseResult;
import com.feidian.domain.entity.User;

public interface LoginService {

    ResponseResult login(User user);

    ResponseResult logout();
}

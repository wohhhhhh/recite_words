package com.feidain.service;

import com.feidain.domain.entity.ResponseResult;
import com.feidain.domain.entity.User;

public interface LoginService {

    ResponseResult login(User user);
}

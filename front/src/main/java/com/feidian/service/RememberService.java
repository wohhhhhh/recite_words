package com.feidian.service;

import com.feidian.domain.dto.RememberConductDTO;
import com.feidian.domain.dto.RememberEndDTO;
import com.feidian.domain.dto.RememberStartDTO;
import com.feidian.domain.entity.ResponseResult;

public interface RememberService {
    ResponseResult startRemember(RememberStartDTO rememberStartDTO);

//    ResponseResult RememberConduct(RememberConductDTO rememberConductDTO);

    ResponseResult RememberEnd(RememberEndDTO rememberEndDTO);
}

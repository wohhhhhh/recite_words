package com.feidian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.feidian.domain.dto.TestConductDTO;
import com.feidian.domain.dto.TestEndDTO;
import com.feidian.domain.dto.TestStartDTO;
import com.feidian.domain.entity.ResponseResult;
import com.feidian.domain.entity.Test;


/**
 * (Test)表服务接口
 *
 * @author makejava
 * @since 2023-05-22 17:33:54
 */
public interface TestService extends IService<Test> {

    ResponseResult viewTestDetail(Integer userId);

    ResponseResult startTest(TestStartDTO testStartDTO);

//    ResponseResult testConduct(TestConductDTO testConductDTO);

    ResponseResult testEnd(TestEndDTO testEndDTO);
}


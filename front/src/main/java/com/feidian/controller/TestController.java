package com.feidian.controller;



import com.feidian.domain.dto.TestConductDTO;
import com.feidian.domain.dto.TestEndDTO;
import com.feidian.domain.dto.TestStartDTO;
import com.feidian.domain.entity.ResponseResult;
import com.feidian.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * (Test)表控制层
 *
 * @author makejava
 * @since 2023-05-22 17:33:52
 */
@RestController
@RequestMapping("/test")
public class TestController{
    @Autowired
    TestService testService;

    @GetMapping("/{user_id}")
    public ResponseResult viewWordBookDetail(@PathVariable("user_id") Integer userId){
        return testService.viewTestDetail(userId);
    }

    @PostMapping("/start")
    public ResponseResult startTest(@RequestBody TestStartDTO testStartDTO){
        return testService.startTest(testStartDTO);
    }
//    @PostMapping("/conduct")
//    public ResponseResult testConduct(@RequestBody TestConductDTO testConductDTO){
//        return testService.testConduct(testConductDTO);
//    }

    @PostMapping("/end")
    public ResponseResult testEnd(@RequestBody TestEndDTO testEndDTO){
        return testService.testEnd(testEndDTO);
    }


}


package com.feidian.controller;

import com.feidian.domain.dto.RememberEndDTO;
import com.feidian.domain.dto.RememberStartDTO;
import com.feidian.domain.entity.ResponseResult;
import com.feidian.service.RememberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/remember")
public class RememberController {
    @Autowired
    RememberService rememberService;

    @PostMapping("/start")
    public ResponseResult startTest(@RequestBody RememberStartDTO rememberStartDTO){
        return rememberService.startRemember(rememberStartDTO);
    }
//    @PostMapping("/conduct")
//    public ResponseResult testConduct(@RequestBody RememberConductDTO rememberConductDTO){
//        return rememberService.RememberConduct(rememberConductDTO);
//    }

    @PostMapping("/end")
    public ResponseResult testEnd(@RequestBody RememberEndDTO rememberEndDTO){
        return rememberService.RememberEnd(rememberEndDTO);
    }
}

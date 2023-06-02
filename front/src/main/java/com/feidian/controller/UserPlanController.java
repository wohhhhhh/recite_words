package com.feidian.controller;



import com.feidian.domain.dto.MakeUserPlanDTO;
import com.feidian.domain.entity.ResponseResult;
import com.feidian.domain.entity.UserPlan;
import com.feidian.service.UserPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * (UserPlan)表控制层
 *
 * @author makejava
 * @since 2023-05-21 15:27:31
 */
@RestController
@RequestMapping("/userPlan")
public class UserPlanController {
    @Autowired
    UserPlanService userPlanService;

    @GetMapping("/{user_plan_id}")
    public ResponseResult viewPlanDetail(@PathVariable("user_plan_id") Integer id){
        return userPlanService.viewPlanDetail(id);
    }

    @PostMapping("/make")
    public ResponseResult makePlan(@RequestBody MakeUserPlanDTO makeUserPlanDTO){
        return userPlanService.makePlan(makeUserPlanDTO);
    }
}


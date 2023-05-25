package com.feidian.controller;



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
//    /**
//     * 服务对象
//     */
//    @Resource
//    private UserPlanService userPlanService;
//
//    /**
//     * 分页查询所有数据
//     *
//     * @param page 分页对象
//     * @param userPlan 查询实体
//     * @return 所有数据
//     */
//    @GetMapping
//    public R selectAll(Page<UserPlan> page, UserPlan userPlan) {
//        return success(this.userPlanService.page(page, new QueryWrapper<>(userPlan)));
//    }
//
//    /**
//     * 通过主键查询单条数据
//     *
//     * @param id 主键
//     * @return 单条数据
//     */
//    @GetMapping("{id}")
//    public R selectOne(@PathVariable Serializable id) {
//        return success(this.userPlanService.getById(id));
//    }
//
//    /**
//     * 新增数据
//     *
//     * @param userPlan 实体对象
//     * @return 新增结果
//     */
//    @PostMapping
//    public R insert(@RequestBody UserPlan userPlan) {
//        return success(this.userPlanService.save(userPlan));
//    }
//
//    /**
//     * 修改数据
//     *
//     * @param userPlan 实体对象
//     * @return 修改结果
//     */
//    @PutMapping
//    public R update(@RequestBody UserPlan userPlan) {
//        return success(this.userPlanService.updateById(userPlan));
//    }
//
//    /**
//     * 删除数据
//     *
//     * @param idList 主键结合
//     * @return 删除结果
//     */
//    @DeleteMapping
//    public R delete(@RequestParam("idList") List<Long> idList) {
//        return success(this.userPlanService.removeByIds(idList));
//    }
    @Autowired
    UserPlanService userPlanService;

    @GetMapping("/{user_plan_id}")
    public ResponseResult viewPlanDetail(@PathVariable("user_plan_id") Integer id){
        return userPlanService.viewPlanDetail(id);
    }

    @PostMapping("/make")
    public ResponseResult makePlan(@RequestBody UserPlan userPlan){
        return userPlanService.makePlan(userPlan);
    }
}


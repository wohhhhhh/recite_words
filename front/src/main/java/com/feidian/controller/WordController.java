package com.feidian.controller;



import com.feidian.domain.entity.ResponseResult;
import com.feidian.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * (Word)表控制层
 *
 * @author makejava
 * @since 2023-05-20 19:55:18
 */
@RestController
@RequestMapping("/word")
public class WordController {
//    /**
//     * 服务对象
//     */
//    @Resource
//    private WordService wordService;
//
//    /**
//     * 分页查询所有数据
//     *
//     * @param page 分页对象
//     * @param word 查询实体
//     * @return 所有数据
//     */
//    @GetMapping
//    public R selectAll(Page<Word> page, Word word) {
//        return success(this.wordService.page(page, new QueryWrapper<>(word)));
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
//        return success(this.wordService.getById(id));
//    }
//
//    /**
//     * 新增数据
//     *
//     * @param word 实体对象
//     * @return 新增结果
//     */
//    @PostMapping
//    public R insert(@RequestBody Word word) {
//        return success(this.wordService.save(word));
//    }
//
//    /**
//     * 修改数据
//     *
//     * @param word 实体对象
//     * @return 修改结果
//     */
//    @PutMapping
//    public R update(@RequestBody Word word) {
//        return success(this.wordService.updateById(word));
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
//        return success(this.wordService.removeByIds(idList));
//    }

    @Autowired
    private WordService wordService;

    @GetMapping("/{word_id}")
    public ResponseResult viewWordBookDetail(@PathVariable("word_id") Long id){
        return wordService.viewWordDetail(id);
    }

    @PostMapping("/search/{word}")
    public ResponseResult searchWordBookDetail(@PathVariable("word") String word){
        return wordService.searchWordDetail(word);
    }


}




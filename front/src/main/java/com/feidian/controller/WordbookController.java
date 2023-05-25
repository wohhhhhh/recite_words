package com.feidian.controller;

import com.feidian.domain.entity.ResponseResult;
import com.feidian.service.WordbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wordbook")
public class WordbookController {
    @Autowired
    private WordbookService wordBookService;

    @GetMapping("/list")
    public ResponseResult showBookList(){
        return wordBookService.showBookList();
    }

    @GetMapping("/{wordbook_id}")
    public ResponseResult viewWordBookDetail(@PathVariable("wordbook_id") Long id){
        return wordBookService.viewWordBookDetail(id);
    }
}

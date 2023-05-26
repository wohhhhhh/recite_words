package com.feidian.controller;

import com.feidian.domain.dto.BookDetailDTO;
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
    public ResponseResult showBookList() {
        return wordBookService.showBookList();
    }
    @GetMapping("/viewWordBookDetail")
    public ResponseResult viewWordBookDetail(@RequestBody BookDetailDTO bookDetailDTO){
        return wordBookService.viewWordBookDetail(bookDetailDTO);
    }
}

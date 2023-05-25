package com.feidian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.feidian.domain.entity.ResponseResult;
import com.feidian.domain.entity.Word;

import java.util.List;


/**
 * (Word)表服务接口
 *
 * @author makejava
 * @since 2023-05-20 19:55:23
 */
public interface WordService extends IService<Word> {

    ResponseResult viewWordDetail(Long id);

    ResponseResult searchWordDetail(String word);

    void deleteWords(List<Word> words);
}


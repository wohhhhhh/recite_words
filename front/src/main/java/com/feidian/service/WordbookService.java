package com.feidian.service;

import com.feidian.domain.entity.ResponseResult;

public interface WordbookService {
    ResponseResult showBookList();

    ResponseResult viewWordBookDetail(Long id);
}

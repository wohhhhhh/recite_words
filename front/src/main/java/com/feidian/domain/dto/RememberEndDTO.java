package com.feidian.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RememberEndDTO {
    private Integer userId;
    //测试单词集合
    private List<RememberWordDTO> rememberWordVOList;
}

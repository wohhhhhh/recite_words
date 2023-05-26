package com.feidian.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestStartDTO {
    // Double 类型
    // 英文选中文比例
    private Integer englishChooseChineseRatio;
    //1是 0不是
    private Integer testNumber;
    private Integer userId;
    private Integer wordbookId;
    private String[] judgingCondition;

}
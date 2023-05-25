package com.feidian.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestStartVO {
    private Integer testId;   // testId
    private Integer testNumber;  // 剩余测试数量
    private Integer userId;
    private Integer wordbookId;

    // 测试题目集合
    private List<TestQuestionVO> testQuestionVOS;

}

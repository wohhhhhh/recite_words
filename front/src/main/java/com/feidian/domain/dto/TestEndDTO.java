package com.feidian.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestEndDTO {
    private Integer userId;
    private Integer testId;
    // 1是英选中，0是中选英
    private Integer isEnglishChooseChinese;

    List<TestQuestionsDTO> testQuestionsDTOS;
}
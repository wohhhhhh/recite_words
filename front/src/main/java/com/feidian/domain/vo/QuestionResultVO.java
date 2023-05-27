package com.feidian.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResultVO {
    private Integer wordId;
    private String wordValue;
    private String wordChineseMeaning;
    private String option;
    private Boolean isCorrect;
}

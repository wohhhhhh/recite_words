package com.feidian.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestQuestionsDTO {
    private Integer word_id;

    private String choice;
    private Boolean isEnglishChooseChinese;
}

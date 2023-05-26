package com.feidian.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestQuestionVO {
    private Integer testQuestionId;
    private Integer wordId;
    private String title;
    private String firstOption;
    private String secondOption;
    private String ThirdOption;
    private String fourOption;
    private Boolean fin;
    private Boolean isEnglishChooseChinese;

}

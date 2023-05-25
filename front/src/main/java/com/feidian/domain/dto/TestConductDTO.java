package com.feidian.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestConductDTO {
    private Integer isEnglishChooseChinese;
    private Integer userId;
    private Integer wordId;
    private String  userChoose;
    private Integer testNumber;
    private Integer testId;

}

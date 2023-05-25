package com.feidian.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RememberConductVO {
    private Integer lastNumber;
    private Integer wordId;
    private Integer userId;
    private String wordValue;
    private String meaningChinese;
}

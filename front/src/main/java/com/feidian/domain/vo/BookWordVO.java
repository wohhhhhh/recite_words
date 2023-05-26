package com.feidian.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookWordVO {
    private Integer id;

    private Integer wordbookId;

    private String value;

    private String meaningChinese;

    private String example;

    private Integer isFamiliar;

    private Integer forgetTime;
}


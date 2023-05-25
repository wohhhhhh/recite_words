package com.feidian.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WordVO {
    private Integer id;

    private Integer wordbookId;

    private String value;

    private String meaningChinese;

    private String example;

}

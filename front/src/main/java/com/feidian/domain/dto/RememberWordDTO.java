package com.feidian.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RememberWordDTO {
    private Integer wordId;
    //1是记得 ，2是模糊，3是不记得
    private Integer rememberState;
}

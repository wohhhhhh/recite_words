package com.feidian.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RememberStartDTO {

    private Integer userId;
    private Integer rememberNumber;
    private Integer wordbookId;
    private String[] judgingCondition;
}

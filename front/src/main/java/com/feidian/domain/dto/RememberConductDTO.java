package com.feidian.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RememberConductDTO {
    private Integer userId;
    private Integer memoryState;
    private Integer wordId;
}

package com.feidian.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MakeUserPlanDTO {
    private Integer userId;

    private Integer wordbookId;

    private LocalDate startDate;

    private Integer totalDays;
}

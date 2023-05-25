package com.feidian.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestDetailVO {
    private Integer id;

    private Integer userId;

    private Integer wordbookId;

    private String wordIds;

    private LocalDate startTime;

    private LocalDate endTime;

    private LocalTime testDuration;

    private Integer accuracy;

    private Integer testNumber;
}

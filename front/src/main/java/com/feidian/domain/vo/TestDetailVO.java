package com.feidian.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestDetailVO {
    private Integer id;

    private Integer userId;

    private Integer wordbookId;

    private LocalDate startTime;

    private LocalDate endTime;

    private LocalTime testDuration;

    private Integer accuracy;

    private Integer testNumber;

    private List<WordVO> wordVOList;

}

package com.feidian.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPlanVO {
    private Integer id;

    private Integer userId;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer totalDays;

    private Integer wordsPerDay;

    private Integer finishedDays;

    private Integer finishedWords;

    private Integer todayFinishedWords;

    private Integer inPlanFinishedWords;

    private Integer wordsToReview;

    private Date gmtCreate;

    private Date gmtModified;


}

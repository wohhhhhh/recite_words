package com.feidian.domain.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WordBookIntroduceVO {
    private Long id;
    private String name;
    private String intro;
    private String suitableUsers;
    private Date gmtCreate;
    private Date gmtModified;
}

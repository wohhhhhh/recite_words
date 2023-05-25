package com.feidian.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestEndVO {
    private TestDetailVO testDetailVO;
    private List<QuestionResultVO> questionResults;
}

package com.feidian.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestConductVO {

    private Integer testId;   // testId

    private Integer testNumber;  // 剩余测试数量

    private Integer userId;
    // 是否正确
    private Boolean isCorrect;

    private WordVO wordVO;

    private List<String> choices;


    public List<String> getChoices() {
        return choices;
    }

    public void setChoices(List<String> choices) {
        this.choices = choices;
    }


}

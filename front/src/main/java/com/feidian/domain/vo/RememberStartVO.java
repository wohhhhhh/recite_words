package com.feidian.domain.vo;

import com.feidian.domain.entity.Word;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RememberStartVO {
    private Integer userId;
    private Boolean fin;
    private Boolean iscover;
    private List<Word> wordList;
}

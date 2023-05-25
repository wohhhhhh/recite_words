package com.feidian.domain.vo;


import com.feidian.domain.entity.UserWord;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RememberEndVO {
    private int userId;
    private List<UserWord> userWords;
}

package com.feidian.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.feidian.domain.entity.Test;
import com.feidian.domain.entity.UserWord;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * (Test)表数据库访问层
 *
 * @author makejava
 * @since 2023-05-22 17:33:55
 */
public interface TestMapper extends BaseMapper<Test> {
    List<Integer> selectRandomWordIds(@Param("testNumber") int testNumber,@Param("wordbookId") int wordbookId,@Param("userWords") List<UserWord> userWords);
}


package com.feidain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.feidain.domain.entity.User;
import org.mybatis.spring.annotation.MapperScan;


/**
 * (User)表数据库访问层
 *
 * @author makejava
 * @since 2023-05-19 14:58:43
 */
@MapperScan("com.feidian.mapper")
public interface UserMapper extends BaseMapper<User> {

}


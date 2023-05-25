package com.feidian.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.feidian.domain.entity.User;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * (User)表数据库访问层
 *
 * @author makejava
 * @since 2023-05-19 14:58:43
 */

@SpringBootApplication
@MapperScan("com.feidian.mapper")
public interface UserMapper extends BaseMapper<User> {

}


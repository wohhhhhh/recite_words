package com.feidian.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feidian.domain.entity.UserWord;
import com.feidian.mapper.UserWordMapper;
import com.feidian.service.UserWordService;
import org.springframework.stereotype.Service;

/**
 * (UserWord)表服务实现类
 *
 * @author makejava
 * @since 2023-05-22 19:06:03
 */
@Service("userWordService")
public class UserWordServiceImpl extends ServiceImpl<UserWordMapper, UserWord> implements UserWordService {

}

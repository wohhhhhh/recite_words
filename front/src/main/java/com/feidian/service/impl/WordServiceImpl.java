package com.feidian.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feidian.domain.entity.ResponseResult;
import com.feidian.domain.entity.Word;
import com.feidian.domain.vo.WordVO;
import com.feidian.enums.AppHttpCodeEnum;
import com.feidian.mapper.WordMapper;
import com.feidian.service.WordService;
import com.feidian.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (Word)表服务实现类
 *
 * @author makejava
 * @since 2023-05-20 19:55:24
 */
@Service("wordService")
public class WordServiceImpl extends ServiceImpl<WordMapper, Word> implements WordService {
    @Autowired
    WordService wordService;

    @Autowired
    WordMapper wordMapper;

    @Override
    public ResponseResult viewWordDetail(Long id) {
        //根据id查询单词信息
        Word word = getById(id);
        //封装成UserInfoVo
        WordVO vo = BeanCopyUtils.copyBean(word,WordVO.class);
        return ResponseResult.okResult(vo);
    }

    @Override
    public ResponseResult searchWordDetail(String value) {
        //查询单词 封装成ResponseResult返回
        LambdaQueryWrapper<Word> queryWrapper = new LambdaQueryWrapper<>();
        //必须是word字段值为传入的word
        queryWrapper.eq(Word::getValue, value);
        //查询Word实体
        Word oneWord = wordService.getOne(queryWrapper);
        if (oneWord != null) { // 如果存在该单词
            //将Word实体复制为WordVO
            WordVO vo = BeanCopyUtils.copyBean(oneWord,WordVO.class);
            return ResponseResult.okResult(vo);
        } else { // 如果不存在该单词
            return ResponseResult.errorResult(AppHttpCodeEnum.NOT_THIS_WORD.getCode(),"没有该单词");
        }
    }

    @Override
    public void deleteWords(List<Word> words) {
        for (Word word : words) {
            LambdaQueryWrapper<Word> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Word::getId, word.getId());
            wordMapper.delete(queryWrapper);
        }
    }


}

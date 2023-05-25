package com.feidian.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.feidian.domain.entity.ResponseResult;
import com.feidian.domain.entity.Word;
import com.feidian.domain.entity.Wordbook;
import com.feidian.domain.vo.WordBookIntroduceVO;
import com.feidian.domain.vo.WordVO;
import com.feidian.mapper.WordMapper;
import com.feidian.mapper.WordbookMapper;
import com.feidian.service.WordbookService;
import com.feidian.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WordbookServiceImpl implements WordbookService {
    @Autowired
    WordbookMapper wordbookMapper;

    @Override
    public ResponseResult showBookList() {
        //查询所有的单词书信息
        List<Wordbook> bookList = wordbookMapper.selectList(null);

        //创建返回结果列表
        List<WordBookIntroduceVO> voList = new ArrayList<>();

        //遍历单词书列表,将每个单词书封装为WordBookVo
        for(Wordbook book : bookList) {
            WordBookIntroduceVO vo = BeanCopyUtils.copyBean(book,WordBookIntroduceVO.class);
            voList.add(vo);
        }

        //返回成功结果集包含所有的单词书vo
        return ResponseResult.okResult(voList);
    }
    @Autowired
    WordMapper wordMapper;

    @Override
    public ResponseResult viewWordBookDetail(Long id) {
        // 查询单词书信息
        Wordbook wordbook = wordbookMapper.selectById(id);
        // 查询该单词书包含的单词,进行分页
        LambdaQueryWrapper<Word> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Word::getWordbookId, id);
        Page<Word> page = new Page<>(1, 10);
        wordMapper.selectPage(page, queryWrapper);

        // 查询单词列表
        List<Word> words = page.getRecords();

        WordBookIntroduceVO wordbookVO = BeanCopyUtils.copyBean(wordbook, WordBookIntroduceVO.class);

        List<WordVO> wordVOS = BeanCopyUtils.copyBeanList(words, WordVO.class);

        Map<String, Object> map = new HashMap<>();
        map.put("wordbook", wordbookVO);
        map.put("words", wordVOS);

        return ResponseResult.okResult(map);
    }
}

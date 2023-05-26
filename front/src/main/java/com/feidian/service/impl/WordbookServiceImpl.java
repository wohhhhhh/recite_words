package com.feidian.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.feidian.contant.SystemConstants;
import com.feidian.domain.dto.BookDetailDTO;
import com.feidian.domain.entity.ResponseResult;
import com.feidian.domain.entity.UserWord;
import com.feidian.domain.entity.Word;
import com.feidian.domain.entity.Wordbook;
import com.feidian.domain.vo.BookWordVO;
import com.feidian.domain.vo.WordBookIntroduceVO;
import com.feidian.domain.vo.WordVO;
import com.feidian.mapper.UserWordMapper;
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

    @Autowired
    UserWordMapper userWordMapper;

    @Override
    public ResponseResult showBookList() {
        //TODO 用户可以看单词不记得次数和单词状态
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
    public ResponseResult viewWordBookDetail(BookDetailDTO bookDetailDTO) {
        int id = bookDetailDTO.getWordbookId();
        int userId =bookDetailDTO.getUserId();
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

        List<BookWordVO> wordVOS = BeanCopyUtils.copyBeanList(words, BookWordVO.class);
        for (BookWordVO wordVO : wordVOS) {
            LambdaQueryWrapper<UserWord> lambdaQueryWrapper=new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(UserWord::getUserId,userId);
            lambdaQueryWrapper.eq(UserWord::getWordId, wordVO.getId());
            UserWord userWord = userWordMapper.selectOne(lambdaQueryWrapper);
            wordVO.setForgetTime(userWord.getForgetTime());
            if (userWord != null) {
                wordVO.setIsFamiliar(SystemConstants.USER_IS_FAMILIAR);
            } else {
                wordVO.setIsFamiliar(SystemConstants.USER_IS_NOT_FAMILIAR);
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("wordbook", wordbookVO);
        map.put("words", wordVOS);
        LambdaQueryWrapper<UserWord> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserWord::getUserId,userId);
        lambdaQueryWrapper.eq(UserWord::getIsFamiliar, SystemConstants.USER_IS_FAMILIAR);
        List<UserWord> userWords = userWordMapper.selectList(lambdaQueryWrapper);
        return ResponseResult.okResult(map);

    }
}

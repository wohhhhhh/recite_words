package com.feidian.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.feidian.contant.SystemConstants;
import com.feidian.domain.dto.RememberConductDTO;
import com.feidian.domain.dto.RememberEndDTO;
import com.feidian.domain.dto.RememberStartDTO;
import com.feidian.domain.entity.*;
import com.feidian.domain.vo.*;
import com.feidian.enums.AppHttpCodeEnum;
import com.feidian.handler.exception.SystemException;
import com.feidian.mapper.UserWordMapper;
import com.feidian.mapper.WordMapper;
import com.feidian.mapper.WordbookMapper;
import com.feidian.service.RememberService;
import com.feidian.service.UserWordService;
import com.feidian.service.WordService;
import com.feidian.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RememberServiceImpl implements RememberService {
    @Autowired
    WordbookMapper wordbookMapper;

    @Autowired
    WordService wordService;

    @Autowired
    WordMapper wordMapper;

    @Autowired
    UserWordMapper userWordMapper;

    @Autowired
    UserWordService userWordService;

    int rememberNumber=0;
    int lastNumber=0;
    int userId=0;
    Set<Word> rememberWords=new TreeSet<>();
    // 刚开始第一轮的索引
    // int beginIndex=0;
    // 循环索引
    // int circleIndex=0;


//    @Override
//    public ResponseResult startRemember(RememberStartDTO rememberStartDTO) {
//        // 获取剩余数量
//        lastNumber=rememberStartDTO.getRememberNumber();
//        // 获取记忆总数量
//        rememberNumber=rememberStartDTO.getRememberNumber();
//        // 获取userId
//        userId=rememberStartDTO.getUserId();
//        // 根据记忆数量和wordbookId来找单词
//        LambdaQueryWrapper<Word> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(Word::getWordbookId, rememberStartDTO.getWordbookId());
//        List<Word> words = wordService.list(queryWrapper);
//        // 创建UserWord对象
//        for (Word word : words) {
//            UserWord userWord = new UserWord();
//            userWord.setUserId(userId);
//            userWord.setWordId(word.getId());
//            userWord.setReviewTimesFamiliar(0);
//            userWord.setReviewTimesVague(0);
//            Date date=new Date();
//            userWord.setGmtCreate(date);
//            userWord.setGmtModified(date);
//            userWordService.save(userWord);
//        }
//        rememberWords.clear();
//        // 根据记忆数量挑选其中的单词
//        for (int i = 0; i < 3; i++) {
//            int index = (int) (Math.random() * rememberStartDTO.getRememberNumber());
//            Word word = words.get(index);
//            rememberWords.add(word);
//        }
//        Word word=rememberWords.get(beginIndex);
//        RememberStartVO vo=new RememberStartVO();
//        vo.setUserId(userId);
//        vo.setLastNumber(lastNumber);
//        vo.setWordId(word.getId());
//        vo.setWordValue(word.getValue());
//        vo.setMeaningChinese(word.getMeaningChinese());
//
//        return ResponseResult.okResult(vo);
//    }

    @Override
    public ResponseResult startRemember(RememberStartDTO rememberStartDTO) {
        int userId = rememberStartDTO.getUserId();
        rememberNumber = rememberStartDTO.getRememberNumber();
        int wordbookId = rememberStartDTO.getWordbookId();

        //根据判断条件选择Word集合
        LambdaQueryWrapper<UserWord> userWordLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userWordLambdaQueryWrapper.eq(UserWord::getUserId,userId);
        LambdaQueryWrapper<Word> wordLambdaQueryWrapper = new LambdaQueryWrapper<>();
        String[] judgingCondition = rememberStartDTO.getJudgingCondition();
        if (judgingCondition !=null){
            List<String> list = Arrays.asList(judgingCondition);
            boolean contains = list.contains("单词书");
            if (contains) {
                wordLambdaQueryWrapper.eq(Word::getWordbookId, wordbookId);
            }
            contains = list.contains("已背单词");
            if (contains) {
                userWordLambdaQueryWrapper.eq(UserWord::getIsFamiliar,SystemConstants.USER_IS_FAMILIAR);
            }
            contains = list.contains("未背单词");
            if (contains) {
                userWordLambdaQueryWrapper.eq(UserWord::getIsFamiliar,SystemConstants.USER_IS_NOT_FAMILIAR);
            }
        }

        //单词书的wordId
        List<Word> wordList=wordMapper.selectList(wordLambdaQueryWrapper);
        Set<Integer> firstWordIds =new TreeSet<>();
        firstWordIds = wordList.stream()
                .map(word -> word.getId())
                .collect(Collectors.toSet());

        List<UserWord> userWordList=userWordMapper.selectList(userWordLambdaQueryWrapper);
        Set<Integer> secondWordIds =new TreeSet<>();
        secondWordIds = userWordList.stream()
                .map(userWord -> userWord.getWordId())
                .collect(Collectors.toSet());
        firstWordIds.addAll(secondWordIds);
        Set<Integer> testIds = firstWordIds.stream()
                .distinct()
                .limit(rememberNumber)
                .collect(Collectors.toSet());
        List<Word> words = testIds.stream()
                .map(id -> wordMapper.selectById(id))   //根据id查询Word
                .collect(Collectors.toList());


        // 使用BeanCopyUtils拷贝对象
        List<Word> wordVOS = BeanCopyUtils.copyBeanList(words, Word.class);
        RememberStartVO vo =new RememberStartVO(userId,wordVOS);
        return ResponseResult.okResult(vo);
        //TODO 构建UserWord对象
    }

//    // 记忆模糊的单词集合
//    List<Word> memoryBlurWords=new ArrayList<>();
//    // 不记得的单词集合
//    List<Word> unrememberedWords=new ArrayList<>();
    // 上个单词的记忆状态
//    int lastNumberMemoryState = 3;
//    @Override
//    public ResponseResult RememberConduct(RememberConductDTO rememberConductDTO) {
//
//        // 先根据单词id和单词状态把单词放到适当的集合里
//        if (rememberConductDTO.getMemoryState()==1){
//            // 记得
//            lastNumber--;
//            LambdaQueryWrapper<UserWord> queryWrapper = new LambdaQueryWrapper<>();
//            queryWrapper.eq(UserWord::getUserId, rememberConductDTO.getUserId());
//            queryWrapper.eq(UserWord::getWordId, rememberConductDTO.getWordId());
//            UserWord userWord=userWordMapper.selectOne(queryWrapper);
//            userWord.setIsFamiliar(1);
//            userWord.setIsFamiliar(1);
//        } else if (rememberConductDTO.getMemoryState()==2) {
//            LambdaQueryWrapper<Word> queryWrapper = new LambdaQueryWrapper<>();
//            queryWrapper.eq(Word::getId, rememberConductDTO.getWordId());
//            Word word = wordService.getOne(queryWrapper);
//            memoryBlurWords.add(word);
//            //模糊
//        } else if (rememberConductDTO.getMemoryState()==3) {
//            //不记得
//            LambdaQueryWrapper<Word> queryWrapper = new LambdaQueryWrapper<>();
//            queryWrapper.eq(Word::getId, rememberConductDTO.getWordId());
//            Word word = wordService.getOne(queryWrapper);
//            unrememberedWords.add(word);
//        }else {
//            throw new SystemException(AppHttpCodeEnum.MEMORYSTATE_NOT_CORRECT);
//        }
//        // 选下一个word
//        Word word=new Word();
//        List<Word> circleWord = new ArrayList<>();
//        circleWord.addAll(unrememberedWords);
//        circleWord.addAll(memoryBlurWords);
//        beginIndex++;
//        // 如果循环集合结束，重新置0
//        if (circleIndex==circleWord.size()-1){
//            circleIndex=0;
//        }
//        if(beginIndex<rememberNumber){
//            word=rememberWords.get(beginIndex);
//        } else if (circleWord.size()!=0) {
//            word=circleWord.get(circleIndex);
//            circleIndex++;
//        }
//        RememberConductVO vo=new RememberConductVO();
//        vo.setUserId(userId);
//        vo.setLastNumber(lastNumber);
//        vo.setWordId(word.getId());
//        vo.setWordValue(word.getValue());
//        vo.setMeaningChinese(word.getMeaningChinese());
//        return ResponseResult.okResult(vo);
//    }

    @Override
    public ResponseResult RememberEnd(RememberEndDTO rememberEndDTO) {
        LambdaQueryWrapper<UserWord> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(UserWord::getUserId,rememberEndDTO.getUserId());
        List<UserWord> userWords = userWordMapper.selectList(queryWrapper);
        RememberEndVO vo=new RememberEndVO();
        vo.setUserId(userId);
        vo.setUserWords(userWords);
        return ResponseResult.okResult(vo);
    }
}

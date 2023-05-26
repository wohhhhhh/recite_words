package com.feidian.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.feidian.contant.SystemConstants;
import com.feidian.domain.dto.RememberConductDTO;
import com.feidian.domain.dto.RememberEndDTO;
import com.feidian.domain.dto.RememberStartDTO;
import com.feidian.domain.dto.RememberWordDTO;
import com.feidian.domain.entity.*;
import com.feidian.domain.vo.*;
import com.feidian.enums.AppHttpCodeEnum;
import com.feidian.handler.exception.SystemException;
import com.feidian.mapper.UserMapper;
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

    @Autowired
    UserMapper userMapper;

    int rememberNumber = 0;
    int lastNumber = 0;
    int userId = 0;
//    Set<Word> rememberWords = new TreeSet<>();
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
        boolean isRememberNumberToBig = rememberNumber > 40;
        if (isRememberNumberToBig) {
            return ResponseResult.errorResult(AppHttpCodeEnum.REMEMBERNUMBER_TO_BIG,"测试数量超过40，一次别记太多效果不好");
        }
        int wordbookId = rememberStartDTO.getWordbookId();
        User user = userMapper.selectById(userId);
        int memoryCount = user.getMemoryCount();
        //根据判断条件选择Word集合
        LambdaQueryWrapper<UserWord> userWordLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userWordLambdaQueryWrapper.eq(UserWord::getUserId, userId);
        LambdaQueryWrapper<Word> wordLambdaQueryWrapper = new LambdaQueryWrapper<>();
        String[] judgingCondition = rememberStartDTO.getJudgingCondition();
        if (judgingCondition != null) {
            List<String> list = Arrays.asList(judgingCondition);
            boolean contains = list.contains("单词书");
            if (contains) {
                wordLambdaQueryWrapper.eq(Word::getWordbookId, wordbookId);
            }
            contains = list.contains("已背单词");
            if (contains) {
                userWordLambdaQueryWrapper.eq(UserWord::getIsFamiliar, SystemConstants.USER_IS_FAMILIAR);
            }
            contains = list.contains("未背单词");
            if (contains) {
                userWordLambdaQueryWrapper.eq(UserWord::getIsFamiliar, SystemConstants.USER_IS_NOT_FAMILIAR);
            }
        }

        // 判断该取什么记忆状态的userWord对象
        List<UserWord> userWordList = selectByMemoryCount(userWordLambdaQueryWrapper, memoryCount);
        //单词书的wordId
        List<Word> wordList = wordMapper.selectList(wordLambdaQueryWrapper);
        Set<Integer> firstWordIds = new TreeSet<>();
        firstWordIds = wordList.stream()
                .map(word -> word.getId())
                .collect(Collectors.toSet());
        Set<Integer> secondWordIds = new TreeSet<>();
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
        for (Word word : words) {
            insertUserWord(userId, word.getId());
        }


        // 使用BeanCopyUtils拷贝对象
        List<RememberWordVO> wordVOS = BeanCopyUtils.copyBeanList(words, RememberWordVO.class);
        for (RememberWordVO wordVO : wordVOS) {
            wordVO.setFin(false);
            wordVO.setIscover(false);
        }
        RememberStartVO vo = new RememberStartVO(userId, wordVOS);
        return ResponseResult.okResult(vo);
    }

    private List<UserWord> selectByMemoryCount(LambdaQueryWrapper<UserWord> userWordLambdaQueryWrapper, int memoryCount) {
        Set<Integer> memoryStateSet = new HashSet<>();
        memoryStateSet.add(0);

        if (memoryCount % 2 == 0) memoryStateSet.add(1);
        if (memoryCount % 3 == 0) memoryStateSet.add(2);
        if (memoryCount % 5 == 0) memoryStateSet.add(3);
        if (memoryCount % 7 == 0) memoryStateSet.add(4);

        userWordLambdaQueryWrapper.in(UserWord::getMemoryState, memoryStateSet);

        List<UserWord> userWords = userWordMapper.selectList(userWordLambdaQueryWrapper);
        return userWords;
    }

    /**
     * 插入UserWord对象
     *
     * @param userId 用户id
     * @param wordId 单词id
     */
    private void insertUserWord(int userId, int wordId) {
        // 根据userId和wordId查询UserWord
        UserWord userWord = userWordMapper.selectOne(new LambdaQueryWrapper<UserWord>()
                .eq(UserWord::getUserId, userId)
                .eq(UserWord::getWordId, wordId));

        if (userWord != null) { // 如果已存在,更新
            userWord.setIsFamiliar(0); // 设置为未记住
            userWordMapper.updateById(userWord);
        } else { // 否则构建新对象插入
            UserWord newUserWord = new UserWord();
            newUserWord.setUserId(userId);
            newUserWord.setWordId(wordId);
            newUserWord.setMemoryState(0);// 设置为未记住
            newUserWord.setIsFamiliar(0);
            newUserWord.setReviewTimesFamiliar(0);
            newUserWord.setReviewTimesVague(0);
            Date date = new Date();
            newUserWord.setGmtCreate(date);
            newUserWord.setGmtModified(date);
            userWordMapper.insert(newUserWord);
        }
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
        List<RememberWordDTO> rememberWordVOList = rememberEndDTO.getRememberWordDTOList();
        List<UserWord> userWords = new ArrayList<>();
        for (RememberWordDTO rememberWordDTO : rememberWordVOList) {
            LambdaQueryWrapper<UserWord> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(UserWord::getUserId, rememberEndDTO.getUserId());
            // 获取wordId
            int wordId = rememberWordDTO.getWordId();
            queryWrapper.eq(UserWord::getWordId, wordId);
            UserWord userWord = userWordMapper.selectOne(queryWrapper);
            userWords.add(userWord);
            // 单词莱特纳盒子的级别
            int memoryState = userWord.getMemoryState();
            int rememberState = rememberWordDTO.getRememberState();
            // 判断是否应增加memoryState,当选择记住且memoryState未达到最高级别时应增加
            boolean shouldIncrease = rememberState == SystemConstants.USER_CHOOSE_REMEMBER
                    && memoryState < SystemConstants.USER_MASTERED;

            // 判断是否应减少memoryState,当选择不记住且memoryState未达到最低级别时应减少
            boolean shouldDecrease = rememberState == SystemConstants.USER_CHOOSE_NOT_REMEMBER;
            if (shouldIncrease) {
                memoryState++;
                userWord.setMemoryState(memoryState);
            } else if (shouldDecrease) {
                userWord.setMemoryState(SystemConstants.MEMORY_STATE_LOWEST);
            }
            userWord.setMemoryState(memoryState);
        }

        RememberEndVO vo = new RememberEndVO();
        vo.setUserId(userId);
        vo.setUserWords(userWords);
        return ResponseResult.okResult(vo);
    }
}
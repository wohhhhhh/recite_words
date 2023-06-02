package com.feidian.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feidian.contant.SystemConstants;
import com.feidian.domain.dto.TestEndDTO;
import com.feidian.domain.dto.TestQuestionsDTO;
import com.feidian.domain.dto.TestStartDTO;
import com.feidian.domain.entity.*;
import com.feidian.domain.vo.*;
import com.feidian.mapper.TestMapper;
import com.feidian.mapper.UserWordMapper;
import com.feidian.mapper.WordMapper;
import com.feidian.mapper.WordbookMapper;
import com.feidian.service.TestService;
import com.feidian.service.UserWordService;
import com.feidian.service.WordService;
import com.feidian.utils.BeanCopyUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * (Test)表服务实现类
 *
 * @author makejava
 * @since 2023-05-22 17:33:55
 */
@Service("testService")
public class TestServiceImpl extends ServiceImpl<TestMapper, Test> implements TestService {
    @Autowired
    TestService testService;

    @Autowired
    TestMapper testMapper;

    @Autowired
    WordbookMapper wordbookMapper;

    @Autowired
    WordMapper wordMapper;

    @Autowired
    WordService wordService;

    @Autowired
    UserWordService userWordService;

    @Autowired
    UserWordMapper userWordMapper;

    @Override
    public ResponseResult viewTestDetail(Integer userId) {
        //查询单词 封装成ResponseResult返回
        LambdaQueryWrapper<Test> queryWrapper = new LambdaQueryWrapper<>();
        //必须是word字段值为传入的word
        queryWrapper.eq(Test::getUserId, userId);
        //查询List实体
        List<Test> tests = testService.list(queryWrapper);

        // 如果是测试数据为空，则返回空集合
        if (tests == null || tests.size() == 0) {
            return ResponseResult.okResult(Collections.emptyList());
        }

        // 如果测试数据是单个的
        if (tests.size() == 1) {
            Test test = tests.get(0);
            TestDetailVO testDetailVO = BeanCopyUtils.copyBean(test, TestDetailVO.class);

            // 解析wordIds,查询词条信息
            List<Integer> wordIds = JSONArray.parseArray(test.getWordIds(), Integer.class);
            List<Word> words = wordService.listByIds(wordIds);
            List<WordVO> wordVOS = BeanCopyUtils.copyBeanList(words, WordVO.class);
            testDetailVO.setWordVOList(wordVOS);

            return ResponseResult.okResult(Collections.singletonList(testDetailVO));
        }

        // 有多个测试数据
        List<TestDetailVO> testDetailVOS = new ArrayList<>();
        for (Test test : tests) {
            TestDetailVO testDetailVO = BeanCopyUtils.copyBean(test, TestDetailVO.class);

            // 解析wordIds,查询词条信息
            List<Integer> wordIds = JSONArray.parseArray(test.getWordIds(), Integer.class);
            List<Word> words = wordService.listByIds(wordIds);
            List<WordVO> wordVOS = BeanCopyUtils.copyBeanList(words, WordVO.class);
            testDetailVO.setWordVOList(wordVOS);
            testDetailVOS.add(testDetailVO);
        }

        return ResponseResult.okResult(testDetailVOS);
    }


    int testNumber=2;
    int correct=0;
    //把集合提出来不就都可以用了
    //把单词集合存入test的单词test_words中
    //存放集合这样就能每次都拿单词了

//    @Override
//    public ResponseResult startTest(TestStartDTO testStartDTO) {
//        // 获取参数
//        Integer userId = testStartDTO.getUserId();
//        Integer testNumber = testStartDTO.getTestNumber();
//        Integer wordbookId = testStartDTO.getWordbookId();
//
//        testAllNumber=testNumber;
//        // 构造测试信息
//        Test test = new Test();
//        test.setUserId(userId);
//        test.setTestNumber(testNumber);
//        test.setWordbookId(wordbookId);
//        //获取当前时间
//        LocalDate now = LocalDate.now();
//
//        test.setStartTime(now);
//        LambdaQueryWrapper<Word> queryWrapper = new LambdaQueryWrapper<>();
//
//        // 查询该单词书包含的单词
//        queryWrapper.eq(Word::getWordbookId, wordbookId);
//        words.clear();
//        words = wordMapper.selectList(queryWrapper);
//        // 获取所有的wordId
//        List<Integer> wordIds = words.stream().map(Word::getId).collect(Collectors.toList());
//
//        // 转成JSON格式
//        String wordIdsJson = JSON.toJSONString(wordIds);
//
//        // 保存到test表
//        test.setWordIds(wordIdsJson);
//
//        // 保存到数据库
//        testMapper.insert(test);
//        // 查询单词书信息
//        Wordbook wordbook = wordbookMapper.selectById(wordbookId);
//
//        // 随机选择一个单词
//        int wordIndex = (int) (Math.random() * words.size());
//        Word selectedWord = words.get(wordIndex);
//
//        // 获取其他3个单词(根据isEnglishChooseChinese的值选择中文或英文)
//        List<String> otherOptions = new ArrayList<>();
//        for (int i = 0; i < 3; i++) {
//            int index = (int) (Math.random() * words.size());
//            Word word = words.get(index);
//            if (testStartDTO.getIsEnglishChooseChinese() == 1) {
//                otherOptions.add(word.getMeaningChinese());
//            } else {
//                otherOptions.add(word.getValue());
//            }
//        }
//
//        // 使用BeanCopyUtils拷贝对象
//        TestStartVO vo = BeanCopyUtils.copyBean(test,TestStartVO.class);
//        vo.setWordVO(BeanCopyUtils.copyBean(selectedWord, WordVO.class));
//        vo.setChoices(otherOptions);
//
//        return ResponseResult.okResult(vo);
//    }
    int isChooseWordbook=0;
    int isChooseAlreadyMemorized=0;
    int isChooseNeverMemorized=0;
    @Override
    public ResponseResult startTest(TestStartDTO testStartDTO) {
        // 获取参数
        Integer userId = testStartDTO.getUserId();
        testNumber = testStartDTO.getTestNumber();
        Integer wordbookId = testStartDTO.getWordbookId();
        int englishChooseChineseRatio = testStartDTO.getEnglishChooseChineseRatio();
        // 根据比例计算英文选中文题目数量
        int englishChooseChineseNumber = (int) Math.round(testNumber * englishChooseChineseRatio / 100.0);
        // 构造测试信息
        Test test = new Test();
        test.setUserId(userId);
        test.setTestNumber(testNumber);
        test.setWordbookId(wordbookId);

        //获取当前时间
        LocalDate now = LocalDate.now();
        test.setStartTime(now);

        LambdaQueryWrapper<UserWord> userWordLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userWordLambdaQueryWrapper.eq(UserWord::getUserId,userId);
        LambdaQueryWrapper<Word> wordLambdaQueryWrapper = new LambdaQueryWrapper<>();
        String[] judgingCondition = testStartDTO.getJudgingCondition();
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
        List<UserWord> userWords = userWordMapper.selectList(userWordLambdaQueryWrapper);

        testMapper.selectRandomWordIds(testNumber,wordbookId,userWords);

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


        // 这个毛病
        Set<Integer> testIds = firstWordIds.stream()
                .distinct()
                .limit(testNumber)
                .collect(Collectors.toSet());

        // 从words中排除graspedWordIds中的wordId
        // 保存到数据库
        Integer testId = test.getId();
        List<Word> words = testIds.stream()
                .map(id -> wordMapper.selectById(id))   //根据id查询Word
                .collect(Collectors.toList());

        // 使用BeanCopyUtils拷贝对象
        TestStartVO vo = BeanCopyUtils.copyBean(test,TestStartVO.class);
        vo.setTestId(testId);
        int testSeconds=5*words.size();
        vo.setTestSeconds(testSeconds);
        // 封装TestQuestion
        List<TestQuestionVO> testQuestionVOS=packageTestQuestionVOS(words,englishChooseChineseNumber,test);
        testMapper.insert(test);
        vo.setTestQuestionVOS(testQuestionVOS);
        return ResponseResult.okResult(vo);
    }

    /**
     * 组合生成试题列表
     *
     * @param words                      单词列表
     * @param englishChooseChineseNumber 英文选中文题数量
     * @param test
     * @return 试题列表
     */
    private List<TestQuestionVO> packageTestQuestionVOS(List<Word> words, int englishChooseChineseNumber, Test test) {
        List<TestQuestionVO> testQuestionVOS = new ArrayList<>();
        int testQuestionId = 1;
        // 生成英文选中文试题
        testQuestionVOS.addAll(generateQuestions(words, englishChooseChineseNumber, true, testQuestionId,test));
        testQuestionId += englishChooseChineseNumber;

        // 生成中文选英文试题
        testQuestionVOS.addAll(generateQuestions(words, words.size() - englishChooseChineseNumber, false, testQuestionId, test));
        for (TestQuestionVO testQuestionVO : testQuestionVOS) {
            testQuestionVO.setFin(false);
        }
        return testQuestionVOS;
    }
    /**
     * 生成试题
     *
     * @param words                  单词列表
     * @param number                 生成试题数量
     * @param isEnglishChooseChinese 是否生成英文选中文试题
     * @param startId                试题起始ID
     * @param test
     * @return 试题列表
     */
    private List<TestQuestionVO> generateQuestions(List<Word> words, int number, boolean isEnglishChooseChinese, int startId, Test test) {
        LambdaQueryWrapper<Word> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        List<Word> wordList = wordMapper.selectList(lambdaQueryWrapper);
        // 测试题目集合
        List<TestQuestionVO> testQuestionVOS = new ArrayList<>();
        Collections.shuffle(words);
        List<Integer> testIds =words.stream()
                .map(word -> word.getId())
                .collect(Collectors.toList());
        // 转成JSON格式
        String wordIdsJson = JSON.toJSONString(testIds);
        // 存早了
        // 保存到test
        test.setWordIds(wordIdsJson);
        for (int i = 0; i < number; i++) {
            Word word = words.get(i);
            TestQuestionVO testQuestionVO = new TestQuestionVO();
            testQuestionVO.setWordId(word.getId());
            // 设置试题ID
            testQuestionVO.setTestQuestionId(startId++);
            // 设置是否英文选中文试题
            testQuestionVO.setIsEnglishChooseChinese(isEnglishChooseChinese);
            if (isEnglishChooseChinese) {
                // 设置英文题目
                testQuestionVO.setTitle(word.getValue());
                // 获取个4个选项
                wordList.remove(word);
                List<String> options = getOptions(wordList, word.getMeaningChinese(),isEnglishChooseChinese);
                wordList.add(word);
                //打乱选项集合
                Collections.shuffle(options);
                // 设置4个选项
                testQuestionVO.setFirstOption(options.get(0));
                testQuestionVO.setSecondOption(options.get(1));
                testQuestionVO.setThirdOption(options.get(2));
                testQuestionVO.setFourOption(options.get(3));
                testQuestionVOS.add(testQuestionVO);
            } else {
                // 设置中文题目
                testQuestionVO.setTitle(word.getMeaningChinese());
                // 获取个4个选项
                wordList.remove(word);
                List<String> options = getOptions(wordList, word.getValue(), isEnglishChooseChinese);
                wordList.add(word);
                //打乱选项集合
                Collections.shuffle(options);
                // 设置4个选项
                testQuestionVO.setFirstOption(options.get(0));
                testQuestionVO.setSecondOption(options.get(1));
                testQuestionVO.setThirdOption(options.get(2));
                testQuestionVO.setFourOption(options.get(3));
                testQuestionVOS.add(testQuestionVO);
                }
            }

        return testQuestionVOS;
    }
    /**
     * 获取随机选项
     *
     * @param words                  单词列表
     * @param correct                正确选项文本
     * @param isEnglishChooseChinese
     * @return 包含正确选项的4个选项列表
     */


    private List<String> getOptions(List<Word> words, String correct, boolean isEnglishChooseChinese) {
        List<String> options = new ArrayList<>();
        options.add(correct);

        for (int i = 0; i < 4; i++) {
            Word word = words.get(i);
            String option;
            if (isEnglishChooseChinese){
                option=word.getMeaningChinese();
            } else {
                option=word.getValue();
            }
            if (!options.contains(option)) {
                options.add(option);
            }
        }
        return options;
    }


//    @Override
//    public ResponseResult testConduct(TestConductDTO testConductDTO) {
//        UserWord userWord = userWordService.getOne(new QueryWrapper<UserWord>()
//                .eq("user_id", testConductDTO.getUserId())
//                .eq("word_id", testConductDTO.getWordId()));
//        int testNumber= testConductDTO.getTestNumber();
//        boolean isCorrect = false;
//        // 查询单词书信息
//
//        Word word = wordService.getById(testConductDTO.getWordId());
//        if (testConductDTO.getIsEnglishChooseChinese() == 1) {
//            // 英选中,检查用户选择与单词中文释义是否相同
//            String wordChineseMeaning = word.getMeaningChinese();
//            isCorrect = wordChineseMeaning.equals(testConductDTO.getUserChoose());
//        } else {
//            // 中选英,检查用户选择与单词英文是否相同
//            String wordEnglish = word.getValue();
//            isCorrect = wordEnglish.equals(testConductDTO.getUserChoose());
//        }
//        if (isCorrect) {
//            // 选择正确,将is_familiar设置为1
//            userWord.setIsFamiliar(1);
//            testNumber= testNumber-1;
//            correct++;
//        } else {
//            // 选择错误,将is_familiar设置为0
//            userWord.setIsFamiliar(0);
//        }
//        wordService.deleteWords(words);
//
//
//        userWordService.updateById(userWord);
//
//        // 随机选择一个单词
//        int wordIndex = (int) (Math.random() * words.size());
//        Word selectedWord = words.get(wordIndex);
//
//        // 获取其他3个单词(根据isEnglishChooseChinese的值选择中文或英文)
//        List<String> otherOptions = new ArrayList<>();
//        for (int i = 0; i < 3; i++) {
//            int index = (int) (Math.random() * words.size());
//            Word oneWord = words.get(index);
//            if (testConductDTO.getIsEnglishChooseChinese() == 1) {
//                otherOptions.add(oneWord.getMeaningChinese());
//            } else {
//                otherOptions.add(oneWord.getValue());
//            }
//        }
//        // 使用BeanCopyUtils拷贝对象
//        TestConductVO vo=new TestConductVO();
//        vo.setTestId(testConductDTO.getTestId());
//        vo.setUserId(testConductDTO.getUserId());
//        vo.setTestNumber(testNumber);
//        vo.setIsCorrect(isCorrect);
//        vo.setWordVO(BeanCopyUtils.copyBean(word, WordVO.class));
//        vo.setChoices(otherOptions);
//
//        return ResponseResult.okResult(vo);
//    }

    @Override
    public ResponseResult testEnd(TestEndDTO testEndDTO) {
        TestEndVO testEndVO = new TestEndVO();
        List<QuestionResultVO> questionResults = new ArrayList<>();
        List<TestQuestionsDTO> testQuestionsDTOS = testEndDTO.getTestQuestionsDTOS();
        int correct = 0;
        for (TestQuestionsDTO testQuestionsDTO : testQuestionsDTOS) {
            boolean equals;
            if (testQuestionsDTO!=null) {
                int wordId = testQuestionsDTO.getWordId();
                Word correctAnswer = wordMapper.selectById(wordId);
                Boolean isEnglishChooseChinese =testQuestionsDTO.getIsEnglishChooseChinese();

                String option= testQuestionsDTO.getChoice();
                if (isEnglishChooseChinese) {
                    equals = option.equals(correctAnswer.getMeaningChinese());
                } else {
                    equals = option.equals(correctAnswer.getValue());
                }
                QuestionResultVO questionResultVO = new QuestionResultVO(wordId, correctAnswer.getValue(),correctAnswer.getMeaningChinese(), option,equals);
                questionResults.add(questionResultVO);
            } else {
                equals=false;
            }
            if (equals) {
                correct++;
            }
        }

        testEndVO.setQuestionResults(questionResults);
        Test test = getById(testEndDTO.getTestId());
        int accuracy =0;
        if (testQuestionsDTOS!=null) {
            accuracy = correct * 100 / testQuestionsDTOS.size();
        }
        test.setAccuracy(accuracy);
        Date now = new Date();
        test.setGmtModified(now);
        LocalDate endTime = LocalDate.now();
        test.setEndTime(endTime);
        Date gmtCreate = test.getGmtCreate();
        Instant start = gmtCreate.toInstant();
        Instant end = now.toInstant();
        Duration duration = Duration.between(start,end);
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;
        String timeDuration = hours + ":" + minutes + ":" + seconds;
        test.setTestDuration(timeDuration);
        updateById(test);
        TestDetailVO testDetailVO=BeanCopyUtils.copyBean(test, TestDetailVO.class);
        testEndVO.setTestDetailVO(testDetailVO);
        return ResponseResult.okResult(testEndVO);
    }
}

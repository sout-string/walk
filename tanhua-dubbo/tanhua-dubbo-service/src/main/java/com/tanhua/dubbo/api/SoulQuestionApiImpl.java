package com.tanhua.dubbo.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tanhua.domain.db.Questionoptions;
import com.tanhua.domain.db.QuestionSoul;
import com.tanhua.domain.vo.QuestionOptionsVo;
import com.tanhua.domain.vo.SoulQuestionVo;
import com.tanhua.dubbo.mapper.QuestionOptionsMapper;
import com.tanhua.dubbo.mapper.SoulQuestionMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description
 * @author:Dell
 * @date:2021/5/23 11:49
 */
@Service
@Slf4j
public class SoulQuestionApiImpl implements SoulQuestionApi {
    @Autowired
    SoulQuestionMapper soulQuestionMapper;
    @Autowired
    QuestionOptionsMapper questionOptionsMaper;

    /**
     * 查询相对题目表
     * @param id
     * @return
     */
    @Override
    public List<SoulQuestionVo> findLIstTable(Integer id) {
        //查询数据题目表
        QueryWrapper<QuestionSoul> query = new QueryWrapper<>();
        List<QuestionSoul> result = soulQuestionMapper.selectList(query);
        if(result.isEmpty()){
            return null;
        }
        //通过题目找题目答案选项表
        List<SoulQuestionVo> soulQuestionVoList = result.stream().map((soulQuestion) -> {
            SoulQuestionVo soulQuestionVo = new SoulQuestionVo();
            soulQuestionVo.setQuestion(soulQuestion.getQuestuinstem());
            soulQuestionVo.setId(soulQuestion.getId() + "");
            QueryWrapper<Questionoptions> qw = new QueryWrapper<>();
            qw.eq("question_id", soulQuestion.getId());
            //选项表中有很多选项
            List<Questionoptions> questionOptions = questionOptionsMaper.selectList(qw);

            List<QuestionOptionsVo>  questionOptionsVoList =new ArrayList<>();
            if (questionOptions!=null){
                for (Questionoptions option : questionOptions) {
                    QuestionOptionsVo questionOptionsVo = new QuestionOptionsVo();
                    questionOptionsVo.setId(option.getId() + "");
                    questionOptionsVo.setOption(option.getOptions());
                    questionOptionsVoList.add(questionOptionsVo);
                }
            }
            soulQuestionVo.setOptions(questionOptionsVoList);
            return soulQuestionVo;
        }).collect(Collectors.toList());

        log.info("查询数据题目表{}",soulQuestionVoList);
        return soulQuestionVoList;
    }
}

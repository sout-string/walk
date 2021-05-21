package com.tanhua.dubbo.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tanhua.domain.db.Question;
import com.tanhua.dubbo.mapper.QuestionMapper;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author : TuGen
 * @date : 2021/5/8 23:41
 */
@Service
public class QuestionApiImpl implements QuestionApi {
    @Autowired
    private QuestionMapper questionMapper;
    @Override
    public Question findByUserId(Long id) {
        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", id);
        return questionMapper.selectOne(queryWrapper);
    }

    @Override
    public void saveQuestions(Question oldQuestions) {
        questionMapper.insert(oldQuestions);
    }

    @Override
    public void updateQuestions(Question oldQuestions) {
        questionMapper.updateById(oldQuestions);
    }
}

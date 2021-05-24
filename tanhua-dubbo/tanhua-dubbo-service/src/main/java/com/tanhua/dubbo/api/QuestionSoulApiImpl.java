package com.tanhua.dubbo.api;

import com.tanhua.domain.db.QuestionBank;
import com.tanhua.dubbo.mapper.QuestionSoulMapper;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *  题目表
 */
@Service
public class QuestionSoulApiImpl implements QuestionSoulApi {
    @Autowired
    private QuestionSoulMapper questionSoulMapper;
    /**
     * 根据题目id查询题目表
     *
     * @param questionId
     * @return
     */
    @Override
    public QuestionBank findById(String questionId) {
        QuestionBank questionBank = questionSoulMapper.selectById(questionId);
        return questionBank;
    }
}

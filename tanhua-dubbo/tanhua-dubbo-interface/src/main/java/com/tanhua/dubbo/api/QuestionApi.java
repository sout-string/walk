package com.tanhua.dubbo.api;

import com.tanhua.domain.db.Question;

/**
 * @author : TuGen
 * @date : 2021/5/8 23:41
 */
public interface QuestionApi {
    Question findByUserId(Long id);

    void saveQuestions(Question oldQuestions);

    void updateQuestions(Question oldQuestions);
}

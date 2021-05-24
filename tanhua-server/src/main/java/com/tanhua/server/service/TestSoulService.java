package com.tanhua.server.service;

import com.tanhua.domain.db.User;
import com.tanhua.domain.vo.AnswersVo;
import com.tanhua.dubbo.api.QuestionSoulApi;
import com.tanhua.dubbo.api.QuestionoptionsApi;
import com.tanhua.dubbo.api.SoulReportApi;
import com.tanhua.server.interceptor.UserHolder;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
public class TestSoulService {
    @Reference
    private QuestionoptionsApi questionoptionsApi;
    @Reference
    private QuestionSoulApi questionSoulApi;
    @Reference
    private SoulReportApi soulReportApi;


    /**
     * 提交问卷
     * @param map
     * @return
     */
    public String submitQuestionnaire(Map<String, List<AnswersVo>> map) {
        // 获取登录用户
        User user = UserHolder.getUser();
        // 计算得分
        Integer score=0;
        Integer questionId =0;// 题目id
        // 遍历获取集合中的值
        Collection<List<AnswersVo>> values = map.values();
        for (List<AnswersVo> answers : values) {
            for (AnswersVo answer : answers) {
                // 拿到题目id
                questionId = Integer.valueOf(answer.getQuestionId());
                // 选项id
                String optionId = answer.getOptionId();

            }
        }





        return null;
    }
}

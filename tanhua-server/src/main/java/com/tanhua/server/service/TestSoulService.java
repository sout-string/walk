package com.tanhua.server.service;

import com.tanhua.domain.constant.SoulConstant;
import com.tanhua.domain.db.QuestionBank;
import com.tanhua.domain.db.Questionoptions;
import com.tanhua.domain.db.SoulReport;
import com.tanhua.domain.db.User;
import com.tanhua.domain.exception.TanHuaException;
import com.tanhua.domain.vo.AnswersVo;
import com.tanhua.dubbo.api.QuestionSoulApi;
import com.tanhua.dubbo.api.QuestionoptionsApi;
import com.tanhua.dubbo.api.SoulReportApi;
import com.tanhua.server.interceptor.UserHolder;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class TestSoulService {
    // 选项
    @Reference
    private QuestionoptionsApi questionoptionsApi;
    // 题目
    @Reference
    private QuestionSoulApi questionSoulApi;
    // 报告
    @Reference
    private SoulReportApi soulReportApi;


    /**
     * 提交问卷
     *
     * @param map
     * @return
     */
    public String submitQuestionnaire(Map<String, List<AnswersVo>> map) {
        if (map.isEmpty()) {
          throw new TanHuaException("无效参数");
        }
        // 报告id
        String reportId=null;
        // 获取登录用户
        User user = UserHolder.getUser();
        // 计算得分
        //总分
        Integer score = 0;
        //四个维度分
        Integer chouXiangScore = 0;
        Integer liXingScore = 0;
        Integer waiXiangScore = 0;
        Integer panDuanScore = 0;
        // 遍历获取集合中的值
        Collection<List<AnswersVo>> values = map.values();
        for (List<AnswersVo> answers : values) {
            for (AnswersVo answer : answers) {
                // 拿到题目id
                String questionId = answer.getQuestionId();
                // 选项id
                String optionId = answer.getOptionId();
                // 根据题目id查询题目表
                QuestionBank questionBank = questionSoulApi.findById(questionId);
                // 根据题目和选项id查询选项
                Questionoptions questionoptions = questionoptionsApi.findById(questionBank.getId(), optionId);
                // 拿到选项的得分
                score += questionoptions.getOptionScore();
                if (questionBank.getDimensiontype().equals("抽象")) {
                    chouXiangScore += questionoptions.getOptionScore();
                } else if (questionBank.getDimensiontype().equals("理性")) {
                    liXingScore += questionoptions.getOptionScore();
                } else if (questionBank.getDimensiontype().equals("外向")) {
                    waiXiangScore += questionoptions.getOptionScore();
                } else if (questionBank.getDimensiontype().equals("判断")) {
                    panDuanScore += questionoptions.getOptionScore();
                }
            }

            // 根据用户id查询报告表
            List<SoulReport> soulcommentList = soulReportApi.findByList(user.getId());
            if (soulcommentList.isEmpty()) {
                // 判断用户解锁的等级
                for (SoulReport soulcomment : soulcommentList) {
                    // 初级
                    if (soulcommentList.size() == 0) {                        // 计算百分比
                        Integer chouXiang = (chouXiangScore * 100) / SoulConstant.ABSTRACTTYPE;
                        Integer liXing = (liXingScore * 100) / SoulConstant.REASON;
                        Integer panDuan = (waiXiangScore * 100) / SoulConstant.JUDGMENT;
                        Integer waiXiang = (panDuanScore * 100) / SoulConstant.OUTGOING;
                        // 解锁为中级
                        soulcomment.setLv("1");
                        soulcomment.setOutgoing(waiXiang.toString() + "%");
                        soulcomment.setJudgement(panDuan.toString() + "%");
                        soulcomment.setReason(liXing.toString() + "%");
                        soulcomment.setAbstracttype(chouXiang.toString() + "%");
                        soulcomment.setScore(score);
                        soulReportApi.insert(soulcomment);
                        reportId=soulcomment.getId().toString();
                    } else if (soulcommentList.size() == 0) {
                        // 计算百分比
                        Integer chouXiang = (chouXiangScore * 100) / SoulConstant.ABSTRACTTYPE;
                        Integer liXing = (liXingScore * 100) / SoulConstant.REASON;
                        Integer panDuan = (waiXiangScore * 100) / SoulConstant.JUDGMENT;
                        Integer waiXiang = (panDuanScore * 100) / SoulConstant.OUTGOING;
                        // 解锁为中级
                        soulcomment.setLv("2");
                        soulcomment.setOutgoing(waiXiang.toString() + "%");
                        soulcomment.setJudgement(panDuan.toString() + "%");
                        soulcomment.setReason(liXing.toString() + "%");
                        soulcomment.setAbstracttype(chouXiang.toString() + "%");
                        soulcomment.setScore(score);
                        soulReportApi.insert(soulcomment);
                        reportId=soulcomment.getId().toString();
                    }
                }
            }
        }
            return reportId;
    }
}

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
import com.tanhua.commons.content.SoulReportUtils;
import com.tanhua.domain.db.SoulReport;
import com.tanhua.domain.db.User;
import com.tanhua.domain.db.UserInfo;
import com.tanhua.domain.vo.AnswersVo;
import com.tanhua.domain.vo.SoulReportVo;
import com.tanhua.dubbo.api.SoulReportApi;
import com.tanhua.dubbo.api.UserInfoApi;
import com.tanhua.server.interceptor.UserHolder;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
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
    @Reference
    private UserInfoApi userInfoApi;


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
        String reportId = null;
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
                        reportId = soulcomment.getId().toString();
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
                        reportId = soulcomment.getId().toString();
                    }
                }
            }
        }
        return reportId;
    }


    /**
     * 查看报告结果
     *
     * @param reportId 报告的id
     * @return
     */
    public SoulReportVo queryReport(Integer reportId) {

        SoulReport soulReport = soulReportApi.queryReport(reportId);

        SoulReportVo soulReportVo = new SoulReportVo();
        Integer score = soulReport.getScore();

        Integer minScore;
        Integer maxScore;
        if (score >= 0 && score <= 20) {
            soulReportVo.setConclusion(SoulReportUtils.OWL_CONCLUSION);
            soulReportVo.setCover(SoulReportUtils.OWL_COVER);
            minScore = 0;
            maxScore = 20;
        } else if (score >= 21 && score <= 40) {
            soulReportVo.setConclusion(SoulReportUtils.RABBIT_CONCLUSION);
            soulReportVo.setCover(SoulReportUtils.RABBIT_COVER);
            minScore = 21;
            maxScore = 40;
        } else if (score >= 41 && score <= 55) {
            soulReportVo.setConclusion(SoulReportUtils.FOX_CONCLUSION);
            soulReportVo.setCover(SoulReportUtils.FOX_COVER);
            minScore = 41;
            maxScore = 55;
        } else {
            soulReportVo.setConclusion(SoulReportUtils.LION_CONCLUSION);
            soulReportVo.setCover(SoulReportUtils.LION_COVER);
            minScore = 56;
            maxScore = 200;
        }

        //设置dimensions维度
        ArrayList<Map<String, String>> dimensions = new ArrayList<>();
        Map<String, String> map1 = new HashMap<>();
        map1.put("外项", soulReport.getOutgoing());
        Map<String, String> map2 = new HashMap<>();
        map2.put("判断", soulReport.getJudgement());
        Map<String, String> map3 = new HashMap<>();
        map3.put("理性", soulReport.getReason());
        Map<String, String> map4 = new HashMap<>();
        map4.put("抽象", soulReport.getAbstracttype());
        Collections.addAll(dimensions, map1, map2, map3, map4);
        soulReportVo.setDimensions(dimensions);

        //相似的人，依据动物类型相同判断
        List<Long> userIds = soulReportApi.similar(minScore, maxScore);
        List<UserInfo> userInfoList = userIds.stream().map(userId -> userInfoApi.findById(userId)).collect(Collectors.toList());
        soulReportVo.setSimilarYou(userInfoList);

        return soulReportVo;

    }
}
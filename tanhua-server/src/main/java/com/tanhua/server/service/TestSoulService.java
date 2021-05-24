package com.tanhua.server.service;

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

@Service
public class TestSoulService {

    @Reference
    private UserInfoApi userInfoApi;
    @Reference
    private SoulReportApi soulReportApi;

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

    /**
     * 提交问卷
     *
     * @param map
     * @return
     */
    public String submitQuestionnaire(Map<String, List<AnswersVo>> map) {
        // 获取登录用户
        User user = UserHolder.getUser();
        // 计算得分
        Integer score = 0;
        Integer questionId = 0;// 题目id
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
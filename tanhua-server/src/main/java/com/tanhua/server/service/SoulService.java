package com.tanhua.server.service;

import com.tanhua.commons.content.SoulReportUtils;
import com.tanhua.domain.db.Questionlist;
import com.tanhua.domain.db.SoulReport;
import com.tanhua.domain.vo.SoulListVo;
import com.tanhua.domain.vo.SoulQuestionVo;
import com.tanhua.dubbo.api.QuestionlistApi;
import com.tanhua.dubbo.api.SoulQuestionApi;
import com.tanhua.dubbo.api.SoulReportApi;
import com.tanhua.server.interceptor.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @description
 * @author:Dell
 * @date:2021/5/23 11:05
 */
@Service
@Slf4j
public class SoulService {
    @Reference
    QuestionlistApi questionlistApi;
    @Reference
    SoulQuestionApi soulQuestionApi;
    @Reference
    SoulReportApi soulReportApi;

    /**
     * 测灵魂-问卷列表（学生实战）
     *
     * @return
     */
    public List<SoulListVo> testSoul() {
        //查询数把然后统一封装
        //查询灵魂问卷表
        List<Questionlist> questionlistList = questionlistApi.findAllTable();
        if (questionlistList.isEmpty()) {
            return null;
        }
        List<SoulListVo> soulListVoList = questionlistList.stream().map((Questionlist questionlist) -> {
            SoulListVo soulListVos = new SoulListVo();
            BeanUtils.copyProperties(questionlist, soulListVos);
            soulListVos.setLevel(questionlist.getLv());
            soulListVos.setCover(questionlist.getAvator());
            //根据对映的问卷id
            List<SoulQuestionVo> soulQuestionVoList = soulQuestionApi.findLIstTable(questionlist.getId());
            if (soulQuestionVoList != null) {
                soulListVos.setQuestions(soulQuestionVoList);
            }

            soulListVos.setId(questionlist.getId() + "");
            //默认为0
            //查询数据是否锁住
            List<SoulReport> result = soulReportApi.getReportMap(UserHolder.getUserId());
            soulListVos.setIsLock(SoulReportUtils.YES_LOCK);
            if (CollectionUtils.isNotEmpty(result)) {
                for (SoulReport soulcomment : result) {
                    if (questionlist.getId().toString().equals(soulcomment.getLv())) {
                        soulListVos.setIsLock(SoulReportUtils.NO_LOCK);
                        soulListVos.setId(soulcomment.getId()+"");
                    }
                }
            } else if (questionlist.getId().toString().equals(SoulReportUtils.DEFOULT_LOCK)) {
                soulListVos.setIsLock(SoulReportUtils.NO_LOCK);
            }


            //查询用户答案表中的报告id

            return soulListVos;
        }).collect(Collectors.toList());
        //查询用户问卷报告表
        return soulListVoList;
    }
}

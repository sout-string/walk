package com.tanhua.server.controller;

import com.tanhua.domain.vo.AnswersVo;
import com.tanhua.server.service.TestSoulService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RequestMapping("testSoul")
@RestController
public class TestSoulController {
    @Autowired
    private TestSoulService testSoulService;
    /**
     * 测灵魂-问卷提交
     * @param map
     * @return
     */
    @PostMapping
    public ResponseEntity submitQuestionnaire(@RequestBody Map<String, List<AnswersVo>> map){
        // 调用方法提交
        String reportId=testSoulService.submitQuestionnaire(map);
        // 返回最新报告id
        return ResponseEntity.ok(reportId);
    }
}

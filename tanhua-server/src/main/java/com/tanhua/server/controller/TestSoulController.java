package com.tanhua.server.controller;

import com.tanhua.domain.vo.SoulReportVo;
import com.tanhua.domain.vo.AnswersVo;
import com.tanhua.server.service.TestSoulService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/testSoul")
public class TestSoulController {

    @Autowired
    private TestSoulService testSoulService;

    /**
     * 查看测试结果
     *
     * @param reportId
     * @return
     */
    @GetMapping("/report/{reportId}")
    public ResponseEntity queryReport(@PathVariable Integer reportId) {

        SoulReportVo soulReportVo = testSoulService.queryReport(reportId);
        return ResponseEntity.ok(soulReportVo);
    }

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
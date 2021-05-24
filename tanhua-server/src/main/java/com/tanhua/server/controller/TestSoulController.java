package com.tanhua.server.controller;

import com.tanhua.domain.vo.SoulReportVo;
import com.tanhua.server.service.TestSoulService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/testSoul")
public class TestSoulController {

    @Autowired
    private TestSoulService testSoulService;

    /**
     * 查看测试结果
     * @param reportId
     * @return
     */
    @GetMapping("/report/{reportId}")
    public ResponseEntity queryReport(@PathVariable Integer reportId) {

        SoulReportVo soulReportVo = testSoulService.queryReport(reportId);
        return ResponseEntity.ok(soulReportVo);
    }

}

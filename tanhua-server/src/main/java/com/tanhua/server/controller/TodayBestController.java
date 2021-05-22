package com.tanhua.server.controller;

import com.tanhua.domain.vo.TodayBestVo;
import com.tanhua.server.service.TodayBestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : TuGen
 * @date : 2021/5/10 16:19
 */
@RestController
@RequestMapping("/tanhua")
@Slf4j
public class TodayBestController {
    @Autowired
    private TodayBestService todayBestService;
    /**
     * 查看今日佳人
     * @return
     */
    @GetMapping("/todayBest")
    public ResponseEntity todayBest() {
        log.info("TodayBestController-查看今日佳人");
       TodayBestVo todayBestVo = todayBestService.queryTodayBest();
        return ResponseEntity.ok(todayBestVo);
    }

}

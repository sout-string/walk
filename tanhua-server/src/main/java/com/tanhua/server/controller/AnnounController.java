package com.tanhua.server.controller;

import com.tanhua.server.service.AnnounService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author : TuGen
 * @date : 2021/5/10 18:18
 */
@RestController
@RequestMapping("/messages")
@Slf4j
public class AnnounController {
    @Autowired
    private AnnounService announService;

    /**
     * 分页查询通知
     * @param page
     * @param pagesizeize
     * @return
     */
    @GetMapping("/announcements")
    public ResponseEntity announcements(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pagesizeize) {
        log.info("AnnounController-分页查询通知");
        return announService.announcements(page, pagesizeize);
    }
}

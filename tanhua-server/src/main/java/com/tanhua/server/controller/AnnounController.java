package com.tanhua.server.controller;

import com.tanhua.server.service.AnnounService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author : TuGen
 * @date : 2021/5/10 18:18
 */
@RestController
@RequestMapping("/messages")
public class AnnounController {
    @Autowired
    private AnnounService announService;
    @GetMapping("/announcements")
    public ResponseEntity announcements(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pagesizeize) {
        return announService.announcements(page, pagesizeize);
    }
}

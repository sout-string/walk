package com.tanhua.server.controller;

import com.tanhua.domain.vo.ContactVo;
import com.tanhua.domain.vo.PageResult;
import com.tanhua.server.service.IMService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author : TuGen
 * @date : 2021/5/16 21:27
 */
@RestController
@RequestMapping("/messages")
@Slf4j
public class IMController {
    @Autowired
    private IMService imService;

    /**
     * 添加联系人
     *
     * @param paramMap
     * @return
     */
    @PostMapping("/contacts")
    public ResponseEntity addContacts(@RequestBody Map<String, Long> paramMap) {
        log.info("IMController-添加联系人");
        imService.addContacts(paramMap.get("userId"));
        return ResponseEntity.ok(null);
    }

    /**
     * 分页查询联系人
     *
     * @param page
     * @param pagesize
     * @return
     */
    @GetMapping("/contacts")
    public ResponseEntity queryContactsList(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer pagesize, @RequestParam(required = false) String keyword) {
        log.info("IMController-分页查询联系人");
        PageResult<ContactVo> result = imService.queryContactsList(page, pagesize, keyword);
        return ResponseEntity.ok(result);
    }

    /**
     * 分页查询点赞
     *
     * @param page
     * @param pagesize
     * @return
     */
    @GetMapping("/likes")
    public ResponseEntity queryLikeList(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer pagesize) {
        log.info("IMController-分页查询点赞");
        return imService.messageCommentList(1, page, pagesize);
    }

    /**
     * 分页查询喜欢
     *
     * @param page
     * @param pagesize
     * @return
     */
    @GetMapping("/comments")
    public ResponseEntity queryLoveList(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer pagesize) {
        log.info("IMController-分页查询喜欢");
        return imService.messageCommentList(2, page, pagesize);
    }

    /**
     * 分页查询评论
     *
     * @param page
     * @param pagesize
     * @return
     */
    @GetMapping("/loves")
    public ResponseEntity queryCommentList(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer pagesize) {
        log.info("IMController-分页查询评论");
        return imService.messageCommentList(3, page, pagesize);
    }
}

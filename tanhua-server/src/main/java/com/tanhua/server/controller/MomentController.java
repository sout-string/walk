package com.tanhua.server.controller;

import com.tanhua.domain.mongo.Publish;
import com.tanhua.domain.vo.MomentVo;
import com.tanhua.domain.vo.PageResult;
import com.tanhua.domain.vo.PublishVo;
import com.tanhua.domain.vo.VisitorVo;
import com.tanhua.server.service.CommentService;
import com.tanhua.server.service.MomentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.lang.annotation.Retention;
import java.util.List;


/**
 * @author : TuGen
 * @date : 2021/5/11 19:53
 */
@RestController
@RequestMapping("movements")
@Slf4j
public class MomentController {
    private final Integer TARGET_TYPE_SMALL_VIDEO = 2;
    private final Integer TARGET_TYPE_COMMENT = 3;
    private final Integer TARGET_TYPE_PUBLISH = 1;
    @Autowired
    private MomentService momentService;
    @Autowired
    private CommentService commentService;

    /**
     * 发布新动态
     *
     * @param vo
     * @param imageContent
     * @return
     */
    @PostMapping
    public ResponseEntity postMoment(PublishVo vo, MultipartFile[] imageContent) {
        log.info("MomentController-发布动态");
        momentService.postMoment(vo, imageContent);
        return ResponseEntity.ok(null);
    }

    /**
     * 分页查询所有好友动态
     *
     * @return
     */
    @GetMapping
    public ResponseEntity queryFriendPublishList(@RequestParam(defaultValue = "1") long page, @RequestParam(defaultValue = "10") long pagesize) {
        log.info("MomentController-分页查询好友动态");
        PageResult<MomentVo> result = momentService.queryFriendPublishList(page, pagesize);
        return ResponseEntity.ok(result);
    }

    /**
     * 分页查询推荐动态
     *
     * @param page
     * @param pagesize
     * @return
     */
    @GetMapping("/recommend")
    public ResponseEntity queryRecommendPublishList(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pagesize) {
        log.info("MomentController-分页查询推荐动态");
        page = page < 1 ? 1 : page;
        PageResult<MomentVo> result = momentService.queryRecommendPublishList(page, pagesize);
        return ResponseEntity.ok(result);
    }

    /**
     * 分页查询某个用户的动态
     *
     * @param page
     * @param pagesize
     * @return
     */
    @GetMapping("/all")
    public ResponseEntity queryALLPublishList(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pagesize, @RequestParam(required = false) Long userId) {
        log.info("MomentController-分页查询某个用户动态");
        PageResult<MomentVo> result = momentService.findAllPublishList(page, pagesize, userId);
        return ResponseEntity.ok(result);
    }

    /**
     * 根据动态id查询动态
     * @param publishId
     * @return
     */
    @GetMapping("/{publishId}")
    public ResponseEntity findById(@PathVariable String publishId) {
        //避免visitors功能报错
        if ("visitors".equals(publishId)) {
            return ResponseEntity.ok(null);
        }
        log.info("MomentController-查询特定动态");
        MomentVo momentVo = momentService.findById(publishId);
        return ResponseEntity.ok(momentVo);
    }

    /**
     * 对动态进行点赞
     *
     * @param publishId
     * @return
     */
    @GetMapping("/{publishId}/like")
    public ResponseEntity like(@PathVariable String publishId) {
        log.info("MomentController-对动态进行点赞");
        long likeCount = commentService.like(publishId, TARGET_TYPE_PUBLISH);
        return ResponseEntity.ok(likeCount);

    }

    /**
     * 取消动态点赞
     *
     * @param publishId
     * @return
     */
    @GetMapping("/{publishId}/dislike")
    public ResponseEntity dislike(@PathVariable String publishId) {
        log.info("MomentController-取消动态点赞");
        long likeCount = commentService.dislike(publishId, TARGET_TYPE_PUBLISH);
        return ResponseEntity.ok(likeCount);
    }

    /**
     * 对动态进行喜欢
     *
     * @param publishId
     * @return
     */
    @GetMapping("/{publishId}/love")
    public ResponseEntity love(@PathVariable String publishId) {
        log.info("MomentController-对动态进行喜欢");
        long loveCount = commentService.love(publishId, TARGET_TYPE_PUBLISH);
        return ResponseEntity.ok(loveCount);

    }

    /**
     * 取消动态喜欢
     *
     * @param publishId
     * @return
     */
    @GetMapping("/{publishId}/unlove")
    public ResponseEntity unlove(@PathVariable String publishId) {
        log.info("MomentController-取消动态喜欢");
        long loveCount = commentService.unlove(publishId, TARGET_TYPE_PUBLISH);
        return ResponseEntity.ok(loveCount);
    }

    /**
     * 谁看过我
     * @return
     */
    @GetMapping("/visitors")
    public ResponseEntity queryVisitors() {
        log.info("MomentController-查询谁看过我");
        List<VisitorVo> visitorVoList = momentService.queryVisitors();
        return ResponseEntity.ok(visitorVoList);
    }
}

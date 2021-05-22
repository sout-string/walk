package com.tanhua.server.controller;

import com.tanhua.domain.vo.PageResult;
import com.tanhua.domain.vo.VideoVo;
import com.tanhua.server.service.CommentService;
import com.tanhua.server.service.MomentService;
import com.tanhua.server.service.VideoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author : TuGen
 * @date : 2021/5/14 20:34
 */
@RestController
@RequestMapping("/smallVideos")
@Slf4j
public class VideoController {
    @Autowired
    private VideoService videoService;
    @Autowired
    private CommentService commentService;
    private final Integer TARGET_TYPE_PUBLISH = 1;
    private final Integer TARGET_TYPE_SMALL_VIDEO = 2;
    private final Integer TARGET_TYPE_COMMENT = 3;

    /**
     * 发布小视频
     * @param videoThumbnail
     * @param videoFile
     * @return
     */
    @PostMapping
    public ResponseEntity post(MultipartFile videoThumbnail, MultipartFile videoFile) throws IOException {
        log.info("VideoController-发布小视频");
        videoService.post(videoThumbnail, videoFile);
        return ResponseEntity.ok(null);
    }

    /**
     * 分页查看小视频
     * @param page
     * @param pagesize
     * @return
     */
    @GetMapping
    public ResponseEntity findPage(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pagesize) {
        log.info("VideoController-分页查看小视频");
        page = page< 1 ? 1 : page;
        PageResult<VideoVo> result = videoService.findPage(page, pagesize);
        return ResponseEntity.ok(result);
    }

    /**
     * 关注视频的作者
     * @param userId
     * @return
     */
    @PostMapping("/{id}/userFocus")
    public ResponseEntity followUser(@PathVariable("id") long userId) {
        log.info("VideoController-关注视频作者");
        videoService.followUser(userId);
        return ResponseEntity.ok(null);
    }

    /**
     * 取消视频作者关注
     *
     * @param userId
     * @return
     */
    @PostMapping("/{id}/userUnFocus")
    public ResponseEntity unfollowUser(@PathVariable("id") long userId) {
        log.info("VideoController-取消关注视频作者");
        videoService.unfollowUser(userId);
        return ResponseEntity.ok(null);
    }

    /**
     * 视频点赞
     * @param videoId
     * @return
     */
    @PostMapping("/{samllvideoId}/like")
    public ResponseEntity like(@PathVariable("samllvideoId") String videoId) {
        log.info("VideoController-视频点赞");
        commentService.like(videoId, TARGET_TYPE_SMALL_VIDEO);
        return ResponseEntity.ok(null);
    }

    /**
     * 视频取消点赞
     * @param videoId
     * @return
     */
    @PostMapping("/{samllvideoId}/dislike")
    public ResponseEntity dislike(@PathVariable("samllvideoId") String videoId) {
        log.info("VideoController-取消视频点赞");
        commentService.dislike(videoId, TARGET_TYPE_SMALL_VIDEO);
        return ResponseEntity.ok(null);
    }
}

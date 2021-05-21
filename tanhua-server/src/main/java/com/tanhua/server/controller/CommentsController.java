package com.tanhua.server.controller;

import com.tanhua.domain.mongo.Publish;
import com.tanhua.domain.vo.CommentVo;
import com.tanhua.domain.vo.PageResult;
import com.tanhua.dubbo.api.mongo.CommentApi;
import com.tanhua.server.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author : TuGen
 * @date : 2021/5/13 18:06
 */
@RestController
@RequestMapping("/comments")
public class CommentsController {
    @Autowired
    private CommentService commentService;
    private final Integer TARGET_TYPE_PUBLISH = 1;
    private final Integer TARGET_TYPE_SMALL_VIDEO = 2;
    private final Integer TARGET_TYPE_COMMENT = 3;

    /**
     * 添加动态的评论类型评论
     *
     * @param paramMap
     * @return
     */
    @PostMapping
    public ResponseEntity add(@RequestBody Map<String, String> paramMap) {
        commentService.add(paramMap,TARGET_TYPE_PUBLISH);

        return ResponseEntity.ok(null);
    }

    /**
     * 删除动态的评论类型评论
     *
     * @param paramMap
     * @return
     */
    @PutMapping
    public ResponseEntity delete(@RequestBody Map<String, String> paramMap) {
        commentService.delete(paramMap);
        return ResponseEntity.ok(null);
    }

    /**
     * 通过动态id分页查询评论列表
     *
     * @param movementId
     * @param page
     * @param pagesize
     * @return
     */
    @GetMapping
    public ResponseEntity findPage(String movementId, @RequestParam(defaultValue = "1") Long page, @RequestParam(defaultValue = "10") Long pagesize) {
        //避免错误数据
        page = page > 1 ? page : 1;
        PageResult<CommentVo> result = commentService.findPage(movementId, page, pagesize);
        return ResponseEntity.ok(result);
    }
    /**
     * 对评论进行点赞
     *
     * @param commentId
     * @return
     */
    @GetMapping("/{commentId}/like")
    public ResponseEntity like(@PathVariable String commentId) {
        long likeCount = commentService.like(commentId, TARGET_TYPE_COMMENT);
        return ResponseEntity.ok(likeCount);

    }

    /**
     * 取消评论点赞
     *
     * @param commentId
     * @return
     */
    @GetMapping("/{commentId}/dislike")
    public ResponseEntity dislike(@PathVariable String commentId) {
        long likeCount = commentService.dislike(commentId, TARGET_TYPE_COMMENT);
        return ResponseEntity.ok(likeCount);
    }
    /**
     * 对评论进行点赞
     *
     * @param commentId
     * @return
     */
    @GetMapping("/{commentId}/love")
    public ResponseEntity love(@PathVariable String commentId) {
        long loveCount = commentService.love(commentId, TARGET_TYPE_COMMENT);
        return ResponseEntity.ok(loveCount);

    }

    /**
     * 取评论态点赞
     *
     * @param commentId
     * @return
     */
    @GetMapping("/{commentId}/unlove")
    public ResponseEntity unlove(@PathVariable String commentId) {
        long loveCount = commentService.unlove(commentId, TARGET_TYPE_COMMENT);
        return ResponseEntity.ok(loveCount);
    }
}

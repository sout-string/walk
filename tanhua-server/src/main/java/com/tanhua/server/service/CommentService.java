package com.tanhua.server.service;

import com.mongodb.lang.Nullable;
import com.tanhua.domain.db.UserInfo;
import com.tanhua.domain.mongo.Comment;
import com.tanhua.domain.vo.CommentVo;
import com.tanhua.domain.vo.PageResult;
import com.tanhua.dubbo.api.UserInfoApi;
import com.tanhua.dubbo.api.mongo.CommentApi;
import com.tanhua.dubbo.api.mongo.PublishApi;
import com.tanhua.server.interceptor.UserHolder;
import com.tanhua.server.utils.RelativeDateFormat;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author : TuGen
 * @date : 2021/5/13 16:21
 */
@Service
@Slf4j
public class CommentService {
    private final Integer COMMENT_TYPE_LIKE = 1;
    private final Integer COMMENT_TYPE_COMMENT = 2;
    private final Integer COMMENT_TYPE_LOVE = 3;

    @Reference
    private PublishApi publishApi;
    @Reference
    private CommentApi commentApi;
    @Autowired
    private RedisTemplate redisTemplate;
    @Reference
    private UserInfoApi userInfoApi;

    /**
     * 保存发表评论
     *
     * @param paramMap
     */
    public void add(Map<String, String> paramMap, Integer targetType) {
        Comment comment = createComment(COMMENT_TYPE_COMMENT, null, targetType, paramMap);
        commentApi.save(comment);
    }

    /**
     * 删除已发表的评论
     *
     * @param paramMap
     */
    public void delete(Map<String, String> paramMap, Integer targetType) {
        Comment comment = createComment(COMMENT_TYPE_COMMENT, null, targetType, paramMap);
        commentApi.delete(comment);
    }

    /**
     * 点赞
     *
     * @param targetId   目标id
     * @param targetType 目标类型
     * @return
     */
    public long like(String targetId, Integer targetType) {
        //查询用户信息
        Long loginUserId = UserHolder.getUserId();
        //创建评论，评论类型点赞
        Comment comment = createComment(COMMENT_TYPE_LIKE, targetId, targetType, null);
        //保存评论
        long likeCount = commentApi.save(comment);
        //redis记录用户已经点赞
        String key = "publish_like_" + loginUserId + "_" + targetId;
        redisTemplate.opsForValue().set(key, 1);
        //返回结果
        return likeCount;
    }

    /**
     * 取消点赞
     *
     * @param targetId   目标id
     * @param targetType 目标类型
     * @return
     */
    public long dislike(String targetId, Integer targetType) {
        //查询用户信息
        Long loginUserId = UserHolder.getUserId();
        //创建评论，评论类型点赞
        Comment comment = createComment(COMMENT_TYPE_LIKE, targetId, targetType, null);
        //保存评论
        long likeCount = commentApi.delete(comment);
        //redis取消用户点赞
        String key = "publish_like_" + loginUserId + "_" + targetId;
        redisTemplate.delete(key);
        //返回结果
        return likeCount;
    }

    /**
     * 喜欢
     *
     * @param targetId   目标id
     * @param targetType 目标类型
     * @return
     */
    public long love(String targetId, Integer targetType) {
        //查询用户信息
        Long loginUserId = UserHolder.getUserId();
        //创建评论，评论类型点赞
        Comment comment = createComment(COMMENT_TYPE_LOVE, targetId, targetType, null);
        //保存评论
        long loveCount = commentApi.save(comment);
        //redis记录用户已经点赞
        String key = "publish_love_" + loginUserId + "_" + targetId;
        redisTemplate.opsForValue().set(key, 1);
        //返回结果
        return loveCount;
    }

    /**
     * 取消喜欢
     *
     * @param targetId   目标id
     * @param targetType 目标类型
     * @return
     */
    public long unlove(String targetId, Integer targetType) {
        //查询用户信息
        Long loginUserId = UserHolder.getUserId();
        //创建评论，评论类型点赞
        Comment comment = createComment(COMMENT_TYPE_LOVE, targetId, targetType, null);
        //保存评论
        long loveCount = commentApi.delete(comment);
        //redis取消用户点赞
        String key = "publish_love_" + loginUserId + "_" + targetId;
        redisTemplate.delete(key);
        //返回结果
        return loveCount;
    }

    /**
     * 生产评论对象
     */
    private Comment createComment(long commentType, @Nullable String targetId, @Nullable Integer targetType, @Nullable Map<String, String> paramMap) {
        Comment comment = new Comment();
        if (commentType == COMMENT_TYPE_LIKE) {
            //点赞类型评论
            comment.setCommentType(COMMENT_TYPE_LIKE);
            comment.setUserId(UserHolder.getUserId());

            //补全评论目标类型、ID
            comment.setTargetType(targetType);
            comment.setTargetId(new ObjectId(targetId));
            return comment;
        } else if (commentType == COMMENT_TYPE_LOVE) {
            //喜欢类型评论
            comment.setCommentType(COMMENT_TYPE_LOVE);
            comment.setUserId(UserHolder.getUserId());

            //补全评论目标类型、ID
            comment.setTargetType(targetType);
            comment.setTargetId(new ObjectId(targetId));
            return comment;
        } else if (commentType == COMMENT_TYPE_COMMENT) {
            //评论类型评论
            comment.setCommentType(COMMENT_TYPE_COMMENT);
            comment.setUserId(UserHolder.getUserId());
            //补全评论目标类型、ID
            comment.setTargetType(targetType);
            comment.setContent(paramMap.get("comment"));
            comment.setTargetId(new ObjectId(paramMap.get("movementId")));
            //补全评论内容
            return comment;
        }
        return null;
    }

    public void delete(Map<String, String> paramMap) {
        log.info("有删除评论的请求，功能还未实现");
    }

    public PageResult<CommentVo> findPage(String publishId, Long page, Long pagesize) {
        PageResult result = commentApi.findPage(publishId, page, pagesize);
        List<Comment> items = result.getItems();
        List<CommentVo> list = new ArrayList<>();
        //转化
        return transformToCommentVo(result);
    }

    /**
     * 分页查询动态结果转化为前端接收的结果
     *
     * @param result
     * @return
     */
    private PageResult<CommentVo> transformToCommentVo(PageResult result) {
        //通过推荐表查询动态并构建结果
        List<Comment> items = result.getItems();
        List<CommentVo> list = new ArrayList<>();
        if (items != null) {
            for (Comment comment : items) {
                //创建前端动态对象
                CommentVo commentVo = new CommentVo();
                //查询作者id
                Long userId = comment.getUserId();
                //查询作者信息
                UserInfo userInfo = userInfoApi.findById(userId);
                //添加作者信息
                if (userInfo != null) {
                    BeanUtils.copyProperties(userInfo, commentVo);
                }
                //添加动态信息
                BeanUtils.copyProperties(comment, commentVo);
                commentVo.setId(comment.getId().toHexString());
                commentVo.setCreateDate(RelativeDateFormat.format(new Date(comment.getCreated())));
                commentVo.setHasLiked(0);
                //查询登录用户是否点赞
                String key = "publish_like_" + UserHolder.getUserId() + "_" + commentVo.getId();
                if (redisTemplate.hasKey(key)) {
                    commentVo.setHasLiked(1);
                }
                //存入
                list.add(commentVo);
            }
        }
        result.setItems(list);
        //返回结果
        return result;
    }
}

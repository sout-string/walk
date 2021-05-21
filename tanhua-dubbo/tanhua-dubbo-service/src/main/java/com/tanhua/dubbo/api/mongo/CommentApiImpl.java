package com.tanhua.dubbo.api.mongo;

import com.tanhua.domain.mongo.Comment;
import com.tanhua.domain.mongo.Publish;
import com.tanhua.domain.mongo.Video;
import com.tanhua.domain.vo.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


/**
 * @author : TuGen
 * @date : 2021/5/13 17:07
 */
@Service
@Slf4j
public class CommentApiImpl implements CommentApi {
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 保存评论
     *
     * @param comment
     * @return
     */
    @Override
    public long save(Comment comment) {
        //判断评论目标类型
        if (comment.getTargetType() == 1) {
            //评论目标为动态
            //查询评论目标信息
            ObjectId publishId = comment.getTargetId();
            Publish publish = mongoTemplate.findById(publishId, Publish.class);
            //补全评论信息
            comment.setTargetUserId(publish.getUserId());
            comment.setCreated(System.currentTimeMillis());
            //mongo添加评论记录
            mongoTemplate.insert(comment);
            //更新动态评论情况（点赞、喜欢）
            //更新筛选条件
            Query query = new Query();
            query.addCriteria(Criteria.where("id").is(publishId));
            //更新内容
            Update update = new Update();
            update.inc(comment.getCol(), 1);
            //返回更新后动态对象
            FindAndModifyOptions options = new FindAndModifyOptions();
            options.upsert(true);
            options.returnNew(true);
            Publish newPublish = mongoTemplate.findAndModify(query, update, options, Publish.class);
            //返回数据
            return getCount(comment, newPublish);
        } else if (comment.getTargetType() == 3) {
            //评论目标为评论
            //查询评论目标信息
            ObjectId commentId = comment.getTargetId();
            Comment comment1 = mongoTemplate.findById(commentId, Comment.class);
            //补全评论信息
            comment.setTargetUserId(comment1.getUserId());
            comment.setCreated(System.currentTimeMillis());
            //mongo添加评论记录
            mongoTemplate.insert(comment);
            //更新动态评论情况（点赞、喜欢）
            //更新筛选条件
            Query query = new Query();
            query.addCriteria(Criteria.where("id").is(commentId));
            //更新内容
            Update update = new Update();
            update.inc(comment.getCol(), 1);
            //返回更新后动态对象
            FindAndModifyOptions options = new FindAndModifyOptions();
            options.upsert(true);
            options.returnNew(true);
            Comment newComment = mongoTemplate.findAndModify(query, update, options, Comment.class);
            //返回数据
            return getCount(comment, newComment);
        } else if (comment.getCommentType() == 2) {
            //评论目标为视频
            //查询评论目标信息
            ObjectId videoId = comment.getTargetId();
            Video video = mongoTemplate.findById(videoId, Video.class);
            //补全评论信息
            comment.setTargetUserId(video.getUserId());
            comment.setCreated(System.currentTimeMillis());
            //添加评论记录
            mongoTemplate.insert(comment);
            //更新视频评论情况
            Query query = new Query();
            query.addCriteria(Criteria.where("id").is(videoId));
            //更新内容
            Update update = new Update();
            update.inc(comment.getCol(), 1);
            //返回更新后动态对象
            FindAndModifyOptions options = new FindAndModifyOptions();
            //添加或更新
            options.upsert(true);
            //返回新值
            options.returnNew(true);
            video = mongoTemplate.findAndModify(query, update, options, Video.class);
            log.info("进入了视频评论增加功能，未完成");
            return getCount(comment,video);
        }
        log.error("未知评论目标类型");
        return 0;
    }

    /**
     * 删除评论
     *
     * @param comment
     * @return
     */
    @Override
    public long delete(Comment comment) {
        //查询评论目标信息
        if (comment.getTargetType() == 1) {
            //评论目标为动态
            //mongo删除评论记录
            Query commentQuery = new Query();
            //同一类型评论只支持一个，后面删除评论要加上判断内容？或者是设计缺陷
            commentQuery.addCriteria(Criteria.where("targetId").is(comment.getTargetId()).and("userId").is(comment.getUserId()).and("commentType").is(comment.getCommentType()));
            mongoTemplate.remove(commentQuery, Comment.class);
            //更新动态评论情况（点赞、喜欢）
            //更新筛选条件
            Query query = new Query();
            query.addCriteria(Criteria.where("id").is(comment.getTargetId()));
            //更新内容
            Update update = new Update();
            update.inc(comment.getCol(), -1);
            //返回更新后动态对象
            FindAndModifyOptions options = new FindAndModifyOptions();
            options.upsert(true);
            options.returnNew(true);
            Publish newPublish = mongoTemplate.findAndModify(query, update, options, Publish.class);
            //返回数据
            return getCount(comment, newPublish);
        } else if (comment.getTargetType() == 3) {
            //评论目标为评论
            //mongo删除评论记录
            Query commentQuery = new Query();
            //同一类型评论只支持一个，后面删除评论要加上判断内容？或者是设计缺陷
            commentQuery.addCriteria(Criteria.where("targetId").is(comment.getTargetId()).and("userId").is(comment.getUserId()).and("commentType").is(comment.getCommentType()));
            mongoTemplate.remove(commentQuery, Comment.class);
            //更新动态评论情况（点赞、喜欢）
            //更新筛选条件
            Query query = new Query();
            query.addCriteria(Criteria.where("id").is(comment.getTargetId()));
            //更新内容
            Update update = new Update();
            update.inc(comment.getCol(), -1);
            //返回更新后动态对象
            FindAndModifyOptions options = new FindAndModifyOptions();
            options.upsert(true);
            options.returnNew(true);
            Comment newComment = mongoTemplate.findAndModify(query, update, options, Comment.class);
            //返回数据
            return getCount(comment, newComment);
        } else if (comment.getCommentType() == 2) {
            //评论目标为视频
            //删除评论
            mongoTemplate.remove(new Query(Criteria.where("targetId").is(comment.getTargetId()).and("userId").is(comment.getUserId())), Comment.class);
            //更新删除后信息
            Query query = new Query();
            //更新目标筛选
            query.addCriteria(Criteria.where("id").is(comment.getTargetId()));
            //更新内容
            Update update = new Update();
            update.inc(comment.getCol(), -1);
            //返回更新后动态对象
            FindAndModifyOptions options = new FindAndModifyOptions();
            options.upsert(true);
            options.returnNew(true);
            Video newVideo = mongoTemplate.findAndModify(query, update, options, Video.class);
            return getCount(comment,newVideo);
        }
        log.error("未知评论目标类型");
        return 0;
    }

    /**
     * 根据动态id分页查询评论
     *
     * @param publishId
     * @param page
     * @param pagesize
     * @return
     */
    @Override
    public PageResult findPage(String publishId, Long page, Long pagesize) {
        //查询评论表
        Query query = new Query();
        query.addCriteria(Criteria.where("targetId").is(new ObjectId(publishId)).and("commentType").is(2));
        //统计评论个数
        long total = mongoTemplate.count(query, Comment.class);
        List<Comment> comments = new ArrayList<>();
        if (total > 0) {
            query.with(Sort.by(Sort.Order.desc("created")));
            query.skip((page - 1) * pagesize).limit(pagesize.intValue());
            comments = mongoTemplate.find(query, Comment.class);
        }
        //创建返回结果
        PageResult<Comment> result = new PageResult<>();
        result.setItems(comments);
        result.setCounts(total);
        result.setPage(page);
        result.setPagesize(pagesize);
        result.setPages(total % pagesize > 0 ? (total / pagesize + 1) : total / pagesize);
        return result;
    }

    /**
     * 分页查询点赞、喜欢、评论列表
     * @param page
     * @param pagesize
     * @param commentType
     * @param userId
     * @return
     */
    @Override
    public PageResult findByUserId(Integer page, Integer pagesize, Integer commentType, Long userId) {
        PageResult<Comment> result = new PageResult<>();
        Query query = new Query();
        query.addCriteria(Criteria.where("targetUserId").is(userId).and("commentType").is(commentType));
        long total = mongoTemplate.count(query,Comment.class);
        query.limit(pagesize).skip((page - 1) * pagesize);
        List<Comment> commentList = mongoTemplate.find(query, Comment.class);
        result.setPage(Long.valueOf(page));
        result.setPagesize(Long.valueOf(pagesize));
        result.setCounts(total);
        result.setItems(commentList);
        result.setPages(total % pagesize > 0 ? (total / pagesize + 1) : total / pagesize);
        return result;
    }

    /**
     * 返回评论目标为动态的评论信息
     * @param comment
     * @param newPublish
     * @return
     */
    private long getCount(Comment comment, Publish newPublish) {
        //确定需要返回的类型
        Integer commentType = comment.getCommentType();
        switch (commentType) {
            case 1:
                return newPublish.getLikeCount();
            case 2:
                return newPublish.getCommentCount();
            case 3:
                return newPublish.getLoveCount();
            default:
                return newPublish.getLikeCount();
        }
    }

    /**
     * 返回评论目标为评论的评论信息
     * @param comment
     * @param newComment
     * @return
     */
    private long getCount(Comment comment, Comment newComment) {
        //确定需要返回的类型
        Integer commentType = comment.getCommentType();
        switch (commentType) {
            case 1:
                return newComment.getLikeCount();
            //评论不能有评论就离谱，想办法改，太傻逼
            case 2:
                return 0;
            default:
                return newComment.getLikeCount();
        }
    }

    /**
     * 返回评论目标为视频的评论信息
     * @param comment
     * @param newVideo
     * @return
     */
    private long getCount(Comment comment, Video newVideo) {
        //确定需要返回的类型
        Integer commentType = comment.getCommentType();
        switch (commentType) {
            case 1:
                return newVideo.getLikeCount();
            //评论不能有评论就离谱，想办法改，太傻逼
            case 2:
                return newVideo.getCommentCount();
            default:
                return newVideo.getLikeCount();
        }
    }
}

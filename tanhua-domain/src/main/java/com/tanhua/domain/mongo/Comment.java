package com.tanhua.domain.mongo;

import lombok.Data;
import lombok.Value;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * @author : TuGen
 * @date : 2021/5/12 20:24
 */
@Data
@Document(value="quznzi_comment")
public class Comment implements Serializable {
    /**
     * 评论Id
     */
    @Id
    private ObjectId id;
    /**
     * 评论目标类型：1-动态 2-视频 3-评论
     */
    private Integer targetType;
    /**
     * 评论目标Id
     */
    private ObjectId targetId;
    /**
     * 评论目标作者Id
     */
    private long targetUserId;
    /**
     * 评论类型：1-点赞 2-评论 3-喜欢
     */
    private Integer commentType;
    /**
     * 评论内容
     */
    private String content;
    /**
     * 评论者Id
     */
    private long userId;
    /**
     * 评论的点赞数
     */
    private Integer likeCount = 0;
    /**
     * 评论发表时间
     */
    private long created;

    /**
     * 评论目标需要更新的字段
     */
    public String getCol() {
        return this.commentType == 1 ? "likeCount" : commentType == 2 ? "commentCount" : "loveCount";
    }
}


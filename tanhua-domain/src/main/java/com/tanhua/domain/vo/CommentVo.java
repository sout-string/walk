package com.tanhua.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : TuGen
 * @date : 2021/5/12 20:34
 */
@Data
public class CommentVo implements Serializable {
    /**
     * 评论的id
     */
    private String id;
    /**
     * 评论者头像
     */
    private String avatar;
    /**
     * 评论者昵称
     */
    private String nickname;
    /**
     * 评论内容
     */
    private String content;
    /**
     * 评论时间 hh:mm
     */
    private String createDate;
    /**
     * 评论点赞数
     */
    private Integer likeCount;
    /**
     * 是否点赞
     */
    private Integer hasLiked;

}

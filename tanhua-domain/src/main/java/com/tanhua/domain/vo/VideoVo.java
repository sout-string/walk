package com.tanhua.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : TuGen
 * @date : 2021/5/14 21:19
 */
@Data
public class VideoVo implements Serializable {
    /**
     * 未定
     */
    private String id;
    /**
     * 作者id
     */
    private Long userId;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 封面
     */
    private String cover;
    /**
     * 视频URL
     */
    private String videoUrl;
    /**
     * 签名
     */
    private String signature;
    /**
     * 点赞数量
     */
    private Integer likeCount;
    /**
     * 是否已赞（1是，0否）
     */
    private Integer hasLiked;
    /**
     * 是是否关注 （1是，0否）
     */
    private Integer hasFocus;
    /**
     * 评论数量
     */
    private Integer commentCount;
}

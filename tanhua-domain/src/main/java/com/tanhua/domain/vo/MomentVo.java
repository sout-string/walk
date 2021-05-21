package com.tanhua.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : TuGen
 * @date : 2021/5/11 17:35
 */
@Data
public class MomentVo implements Serializable {
    /**
     * 动态id
     */
    private String id;
    private String avatar;
    private String nickname;
    private String gender;
    private Integer age;
    private String[] tags;


    private String textContent;
    private String[] imageContent;
    private String distance;
    private String createDate;
    /**
     * 点赞
     */
    private int likeCount;
    /**
     * 评论
     */
    private int commentCount;
    /**
     * 喜欢
     */
    private int loveCount;
    /**
     * 点赞状态
     */
    private Integer hasLiked;
    /**喜欢状态*/
    private Integer hasLoved;

}

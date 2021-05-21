package com.tanhua.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 互相喜欢（好友表）
 * @author TuGen
 * @date 2021/5/18 20:00
 */
@Data
public class FriendVo implements Serializable {

    private Long id;
    private String avatar;
    private String nickname;
    private String gender;
    private Integer age;
    private String city;
    private String education;
    /**
     * 婚姻状态（0未婚，1已婚）
     */
    private Integer marriage;
    /**
     * 匹配度
     */
    private Integer matchRate;
    /**
     *  是否喜欢
     */
    private boolean alreadyLove;
}
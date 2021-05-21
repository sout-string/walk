package com.tanhua.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : TuGen
 * @date : 2021/5/7 10:17
 */
@Data
public class UserInfoVo implements Serializable {
    /**
     * 用户id
     */
    private Long id;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 用户头像
     */
    private String avatar;
    /**
     * 生日
     */
    private String birthday;
    /**
     * 性别
     */
    private String gender;
    /**
     * 年龄
     */
    private String age;
    /**
     * 城市
     */
    private String city;
    /**
     * 收入
     */
    private String income;
    /**
     * 学历
     */
    private String education;
    /**
     * 行业
     */
    private String profession;
    /**
     * 婚姻状态
     */
    private Integer marriage;

}

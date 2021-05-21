package com.tanhua.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 统计粉丝等-前端
 * @author TuGen
 * @date 2021/5/18 18:12
 */
@Data
public class CountsVo implements Serializable {
    /**
     * 互相喜欢
     */
    private Long eachLoveCount;
    /**
     * 喜欢
     */
    private Long loveCount;
    /**
     * 粉丝
     */
    private Long fanCount;
}
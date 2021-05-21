package com.tanhua.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 谁看过我-前端
 * @author TuGen
 * @date 2021/5/18 17:29
 */
@Data
public class VisitorVo implements Serializable {
    private Long id;
    private String avatar;
    private String nickname;
    private String gender;
    private Integer age;
    private String[] tags;
    private Integer fateValue; 
}
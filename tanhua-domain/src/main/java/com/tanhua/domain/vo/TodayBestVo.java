package com.tanhua.domain.vo;

import lombok.Data;

/**
 * @author : TuGen
 * @date : 2021/5/10 16:16
 */
@Data
public class TodayBestVo {
    private Integer id;
    private String avatar;
    private String nickname;
    private String gender;
    private Integer age;
    private String[] tags;
    private Integer fateValue;
}

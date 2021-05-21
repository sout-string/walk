package com.tanhua.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author : TuGen
 * @date : 2021/5/10 20:39
 */
@Data
public class RecommendUserVo implements Serializable {
    private Long id;
    private String avatar;
    private String nickname;
    private String gender;
    private Integer age;
    private String[] tags;
    private Long fateValue;

}

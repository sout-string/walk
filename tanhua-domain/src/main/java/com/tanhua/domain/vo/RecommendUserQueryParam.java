package com.tanhua.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author : TuGen
 * @date : 2021/5/10 20:11
 */
@Data
public class RecommendUserQueryParam implements Serializable {
    private Integer page;
    private Integer pagesize;
    private String gander;
    private String city;
    private Integer age;
    private String education;
    private String lastLogin;
}

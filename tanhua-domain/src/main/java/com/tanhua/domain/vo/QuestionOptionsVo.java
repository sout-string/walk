package com.tanhua.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @description
 * @author:Dell
 * @date:2021/5/23 10:07
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionOptionsVo implements Serializable {
    private String  id;
    private String option;
}

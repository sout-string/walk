package com.tanhua.domain.db;

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
public class QuestionOptions implements Serializable {
    private   Integer id;
    private     String  option;
    private     Integer score;
    private     Integer questionId;

    private Integer sort;
}

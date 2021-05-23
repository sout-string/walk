package com.tanhua.domain.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @description
 * @author:Dell
 * @date:2021/5/23 10:17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserReport implements Serializable {
    //用户问卷报告表
    private Integer id;
    private Integer QuestionnaireId;

    private Integer userId;

    private Integer conclusionId;


}

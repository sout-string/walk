package com.tanhua.domain.db;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 *  用户题目表
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_question")
public class QuestionBank implements Serializable {
    // 题目id
    private Integer id;
    // 题目内容
    private String questuinstem;
    // 题目维度类型
    private String dimensiontype;
    // 题目最大分值  (用于计算用户得分)
    private Integer score;

}

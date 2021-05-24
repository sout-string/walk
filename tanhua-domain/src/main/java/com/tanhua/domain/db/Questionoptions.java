package com.tanhua.domain.db;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 *  题目选项表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_questionoptions")
public class Questionoptions implements Serializable {
    // 主键id
    private Integer id;
    // 题目id
    private Integer questionId;
    // 选项id
    private String optionsId;
    // 选项内容
    private String options;
    // 选项评分
    private Integer questionScore;
}

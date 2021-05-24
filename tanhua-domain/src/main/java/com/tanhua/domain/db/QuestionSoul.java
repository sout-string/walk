package com.tanhua.domain.db;

import com.baomidou.mybatisplus.annotation.TableField;
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
public class QuestionSoul implements Serializable {
    private Integer id;
    private String questuinstem;//题目
    private String dimensiontype;//维度类型表
    private Integer score;//最在分值
    //不希望该值存入数据库
    @TableField(exist = false)
    private Questionoptions options;//题目答案选项表
}

package com.tanhua.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @description
 * @author:Dell
 * @date:2021/5/23 10:07
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SoulQuestionVo implements Serializable {
    private String  id;
    private String question;//题目
    private List<QuestionOptionsVo> options;//题目答案选项表
}

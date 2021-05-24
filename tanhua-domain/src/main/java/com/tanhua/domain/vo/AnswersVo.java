package com.tanhua.domain.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 答案
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnswersVo implements Serializable {
    private String questionId;   // 题目id
    private String optionId;     // 选项id
}

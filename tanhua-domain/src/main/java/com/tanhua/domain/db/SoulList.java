package com.tanhua.domain.db;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @description
 * @author:Dell
 * @date:2021/5/23 9:47
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SoulList implements Serializable {
    //灵魂问卷表
    private String id; //灵魂问卷ID号
    private String name;//问卷名称
    private String cover; //封面
    private String level;  //级别
    private Integer star;  //星别
    private Integer isLock; //试题编号
    private String reportId;//最新报告id
    private List<QuestionSoul> questions;//题目表
    private List<Questionoptions> options;//题目答案选项表

}

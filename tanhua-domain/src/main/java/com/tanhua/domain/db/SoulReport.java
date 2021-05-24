package com.tanhua.domain.db;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.io.Serializable;


/**
 * 用户报告表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_soul_report")
public class SoulReport extends BasePojo {
    // 报告表id
    @Id
    private Integer id;
    // 用户id
    private Long userId;
    // 等级
    private String lv;
    // 外向
    private String outgoing;
    // 判断
    private String judgement;
    // 理性
    private String reason;
    // 抽象
    private String abstracttype;
    // 用户得分值
    private Integer score;
}

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
public class SoulQuestion implements Serializable {
    private Integer id;
    private String question;//题目
    private Integer dimensionTypeId;//维度类型表id
    private Integer questionnaireId;//灵魂问卷表id
}

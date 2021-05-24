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
public class Questionoptions implements Serializable {
    private Integer id;
    private Integer questionid;
    private Integer optionsid;
    private String options;
    private Integer questionscore;
}

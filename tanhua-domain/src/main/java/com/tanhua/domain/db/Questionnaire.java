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
@AllArgsConstructor
@NoArgsConstructor
public class Questionnaire implements Serializable {
    private  Integer id;
    private  String name;
    private  String cover;
    private  Integer lv;
    private  Integer star;
}

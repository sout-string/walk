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
@AllArgsConstructor
@NoArgsConstructor
/**
 * 灵魂问卷表
 */

public class Questionlist implements Serializable {
    private  Integer id;
    private  String name;
    private  String avator;
    private  String lv;
    private  Integer star;
}

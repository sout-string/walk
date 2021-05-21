package com.tanhua.domain.db;

import lombok.Data;

/**
 * @author : TuGen
 * @date : 2021/5/8 23:02
 */
@Data
public class Question extends BasePojo{
    private Long id;
    private long userId;
    /**问题*/
    private String txt;
}

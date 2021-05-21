package com.tanhua.domain.db;

import lombok.Data;

import java.util.List;

/**
 * @author : TuGen
 * @date : 2021/5/8 23:03
 */
@Data
public class BlackList extends BasePojo{
    private Long id;
    private Long userId;
    private Long blackUserId;
}

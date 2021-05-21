package com.tanhua.domain.db;

import lombok.Data;

/**
 * @author : TuGen
 * @date : 2021/5/8 22:59
 */
@Data
public class Settings extends BasePojo{
    private Long id;
    private Long userId;
    private Boolean likeNotification;
    private Boolean pinglunNotification;
    private Boolean gonggaoNotification;
}

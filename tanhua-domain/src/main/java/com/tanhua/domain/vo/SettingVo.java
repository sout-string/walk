package com.tanhua.domain.vo;

import lombok.Data;

/**
 * @author : TuGen
 * @date : 2021/5/8 23:05
 */
@Data
public class SettingVo {
    private Long id;
    private String strangerQuestion ;
    private String phone;
    private boolean likeNotifacation;
    private boolean pingLunNotification;
    private boolean gonggaoNotification;
}

package com.tanhua.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author : TuGen
 * @date : 2021/5/10 18:17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnnouncementVo implements Serializable {
    private String id;
    private String title;
    private String description;
    private String createDate;
}

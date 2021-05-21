package com.tanhua.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author : TuGen
 * @date : 2021/5/11 17:33
 */
@Data
public class PublishVo implements Serializable {
    private Long userId;
    private String textContent;
    private String location;
    private String longitude;
    private String latitude;
    /**
     * 图片地址
     */
    private List<String> medias;
}

package com.tanhua.domain.mongo;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

/**
 * 动态表
 *
 * @author : TuGen
 * @date : 2021/5/11 17:02
 */
@Data
@Document(value = "quanzi_publish")
public class Publish implements Serializable {
    @Id
    private ObjectId id;
    /** 推荐系统id */
    private Long pid;
    /** 动态作者id */
    private Long userId;
    private String textContent;
    /** 审核状态 */
    private Integer state;
    /** 图片 */
    private List<String> medias;
    /** 开放级别，谁可以看，1-公开，2-私密，3-部分可见，4-不给谁看 */
    private Integer seeType;
    /** 经纬度 */
    private String longitude;
    private String latitude;
    private String location;
    private Long created;
    /** 点赞数 */
    private Integer likeCount = 0;
    /** 评论数 */
    private Integer commentCount = 0;
    /**
     * 喜欢数
     */
    private Integer loveCount = 0;

}

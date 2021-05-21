package com.tanhua.domain.mongo;

import lombok.Data;
import org.bson.codecs.IntegerCodec;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * @author : TuGen
 * @date : 2021/5/14 20:53
 */
@Data
@Document(value = "video")
public class Video implements Serializable {
    /**
     * 主键id
     */
    private ObjectId id;
    /**
     * 作者id
     */
    private Long userId;
    /**
     * 推荐系统id
     */
    private Long vid;
    /**
     * 文字内容
     */
    private String text;
    /**
     * 视频封面文件地址
     */
    private String picUrl;
    /**
     * 视频文件地址
     */
    private String videoUrl;
    /**
     * 创建时间
     */
    private Long created;
    /**
     * 点赞数
     */
    private Integer likeCount = 0;
    /**
     * 评论数
     */
    private Integer commentCount = 0;
    /**
     * 喜欢数
     */
    private Integer loveCount = 0;
}

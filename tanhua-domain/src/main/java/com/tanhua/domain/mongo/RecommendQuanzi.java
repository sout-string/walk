package com.tanhua.domain.mongo;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * @author : TuGen
 * @date : 2021/5/11 17:29
 */
@Data
@Document(value = "recommend_quanzi")
public class RecommendQuanzi implements Serializable {
    @Id
    private ObjectId id;
    /**
     * 推荐的用户id
     */
    @Indexed
    private Long userId;
    /**
     * 推荐系统id
     */
    private Long pid;
    /**
     * 发布的动态id
     */
    private ObjectId publishId;
    /**
     * 推荐分数
     */
    private Double score = 0d;
    /**
     * 日期
     */
    private Long created;
}

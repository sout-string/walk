package com.tanhua.domain.mongo;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@Document(collection = "visitors")
public class Visitor implements Serializable {

    private ObjectId id;
    /**
     * 我的id
     */
    private Long userId;
    /**
     * 来访用户id
     */
    private Long visitorUserId;
    /**
     * 来源，如首页、圈子等
     */
    private String from;
    /**
     * 来访时间
     */
    private Long date;
    /**
     * 得分
     */
    private Double score;
}
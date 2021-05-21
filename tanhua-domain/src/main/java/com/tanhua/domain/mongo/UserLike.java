package com.tanhua.domain.mongo;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
 
@Data
@Document(collection = "user_like")
public class UserLike implements Serializable {

    private ObjectId id;

    /**
     * 用户id，自己
     */
    @Indexed
    private Long userId;

    /**
     * 喜欢的用户id，对方
     */
    @Indexed
    private Long likeUserId;

    /**
     * 创建时间
     */
    private Long created;
}
package com.tanhua.domain.mongo;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * @author : TuGen
 * @date : 2021/5/15 22:35
 */
@Data
@Document("follow_user")
public class FollowUser implements Serializable {
    private ObjectId id;
    private long userId;
    private Long followUserId;
    private Long created;
}

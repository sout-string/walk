package com.tanhua.domain.mongo;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * 好友关系表
 * @author : TuGen
 * @date : 2021/5/11 17:26
 */
@Data
@Document(value = "user_friend")
public class Friend implements Serializable {
    /**
     *用户识别码
     */
    private ObjectId id;
    private Long userId;
    private Long friendId;
    private Long created;
}

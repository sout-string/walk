package com.tanhua.domain.mongo;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * @author : TuGen
 * @date : 2021/5/10 16:42
 */
@Data
@Document(value = "recommed_user")
public class RecommendUser implements Serializable {
    @Id
    private ObjectId id;
    @Indexed
    private Long toUserId;
    private Long userId;
    @Indexed
    private Double score = 0d;
    private String date;
}

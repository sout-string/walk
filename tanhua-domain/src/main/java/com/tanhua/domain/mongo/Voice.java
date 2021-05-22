package com.tanhua.domain.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author atsing
 * @date 2021/5/22
 */
@Data
@Document(collection = "voice")
public class Voice {
    @Indexed
    private ObjectId id;
    private Integer userId;
    private String filePath;

    public Voice(Integer userId, String filePath) {
        this.userId = userId;
        this.filePath = filePath;
    }
}

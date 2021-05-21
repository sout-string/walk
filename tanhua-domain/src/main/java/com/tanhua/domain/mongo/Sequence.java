package com.tanhua.domain.mongo;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * @author : TuGen
 * @date : 2021/5/11 20:12
 */
@Data
@Document(value = "sequence")
public class Sequence implements Serializable {
    private ObjectId id;
    /**
     * 自增序列
     */
    private long seqId;
    private String collName;
}

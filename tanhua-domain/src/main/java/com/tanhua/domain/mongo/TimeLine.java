package com.tanhua.domain.mongo;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

/**
 * @author : TuGen
 * @date : 2021/5/11 17:20
 */
@Data
public class TimeLine implements Serializable {
    /**
     *用户识别码
     */
    private ObjectId id;
    /**
     * 动态作者Id
     */
    private Long userId;
    /**
     * 动态Id
     */
    private ObjectId publishId;
    /**
     * 发布时间
     */
    private Long created;
}

package com.tanhua.dubbo.api.mongo;

import com.tanhua.domain.mongo.Publish;
import com.tanhua.domain.vo.PageResult;

/**
 * @author : TuGen
 * @date : 2021/5/11 20:10
 */
public interface PublishApi {
    void add(Publish publish);

    PageResult queryFriendPublishList(long page, long pagesize,Long userId);

    PageResult findRecommendPublish(int page, int pagesize, Long loginUserId);

    PageResult findAllPublish(int page, int pagesize, Long userId);

    Publish findById(String publishId);
}

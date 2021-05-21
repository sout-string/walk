package com.tanhua.dubbo.api.mongo;

import com.tanhua.domain.mongo.FollowUser;
import com.tanhua.domain.mongo.Video;
import com.tanhua.domain.vo.PageResult;

/**
 * @author : TuGen
 * @date : 2021/5/14 20:52
 */
public interface VideoApi {
    void save(Video video);

    PageResult findPage(int page, int pagesize);

    void followUser(FollowUser followUser);

    void unfollowUser(FollowUser followUser);
}

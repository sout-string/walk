package com.tanhua.dubbo.api;

import com.tanhua.domain.vo.PageResult;

/**
 * @author : TuGen
 * @date : 2021/5/16 21:33
 */
public interface FriendApi {
    void add(Long loginUserId, Long friendId);

    PageResult findPage(Long userId, Integer page, Integer pagesize, String keyword);
}

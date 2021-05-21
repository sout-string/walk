package com.tanhua.dubbo.api;

import com.tanhua.domain.vo.PageResult;

/**
 * @author : TuGen
 * @date : 2021/5/18 18:21
 */
public interface UserLikeApi {
    Long countLike(Long loginUserId);

    Long countFans(Long loginUserId);

    Long countLikeEachOther(Long loginUserId);

    PageResult findPageLikeEachOther(Long loginUserId, int page, int pagesize);

    PageResult findPageOneSideLike(Long loginUserId, int page, int pagesize);

    PageResult findPageFans(Long loginUserId, int page, int pagesize);

    PageResult findPageMyVisitors(Long loginUserId, int page, int pagesize);
}

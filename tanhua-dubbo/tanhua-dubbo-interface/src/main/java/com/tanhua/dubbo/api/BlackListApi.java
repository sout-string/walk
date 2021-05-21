package com.tanhua.dubbo.api;

import com.tanhua.domain.vo.PageResult;

/**
 * @author : TuGen
 * @date : 2021/5/9 0:52
 */
public interface BlackListApi {
    PageResult findPageBlackList(Long page, Long pagesize, Long userId);

    void deleteBlackList(Long userId,long blackUserId);
}

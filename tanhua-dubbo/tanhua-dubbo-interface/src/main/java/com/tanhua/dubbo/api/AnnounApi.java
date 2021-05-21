package com.tanhua.dubbo.api;

import com.tanhua.domain.db.Announcement;
import com.tanhua.domain.vo.PageResult;

/**
 * @author : TuGen
 * @date : 2021/5/10 19:40
 */
public interface AnnounApi {
    PageResult<Announcement> announcements(int page, int pagesizeize);
}

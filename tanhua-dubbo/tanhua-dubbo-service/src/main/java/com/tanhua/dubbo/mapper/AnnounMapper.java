package com.tanhua.dubbo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tanhua.domain.db.Announcement;

/**
 * @author : TuGen
 * @date : 2021/5/10 19:46
 */
public interface AnnounMapper extends BaseMapper<Announcement> {
    void selectPage(Page<Announcement> pages, int pagesize);
}

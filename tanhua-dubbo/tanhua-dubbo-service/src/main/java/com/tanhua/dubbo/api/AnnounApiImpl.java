package com.tanhua.dubbo.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tanhua.domain.db.Announcement;
import com.tanhua.domain.vo.PageResult;
import com.tanhua.dubbo.mapper.AnnounMapper;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author : TuGen
 * @date : 2021/5/10 19:41
 */
@Service
public class AnnounApiImpl implements AnnounApi {
    @Autowired
    private AnnounMapper announMapper;

    /**
     * 分页查询公告
     * @param page
     * @param pagesize
     * @return
     */
    @Override
    public PageResult<Announcement> announcements(int page, int pagesize) {
        //
        Page<Announcement> pages = new Page<>(page, pagesize);
        IPage<Announcement> announcementIPage = announMapper.selectPage(pages, new QueryWrapper<>());
        PageResult<Announcement> announcementPageResult = new PageResult<Announcement>(announcementIPage.getTotal(), announcementIPage.getSize(), announcementIPage.getPages(), announcementIPage.getCurrent(), announcementIPage.getRecords());
        return announcementPageResult;
    }
}

package com.tanhua.server.service;

import com.tanhua.domain.db.Announcement;
import com.tanhua.domain.vo.AnnouncementVo;
import com.tanhua.domain.vo.PageResult;
import com.tanhua.dubbo.api.AnnounApi;
import org.apache.dubbo.config.annotation.Reference;

import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : TuGen
 * @date : 2021/5/10 18:28
 */
@Service
public class AnnounService {

    @Reference
    private AnnounApi announApi;

    public ResponseEntity announcements(int page, int pagesizeize) {
        //获取公告
         PageResult<Announcement> pageResult= announApi.announcements(page, pagesizeize);
         //转化
        PageResult<AnnouncementVo> voPageResult = new PageResult<>();
        List<AnnouncementVo> voList = new ArrayList<>();
        List<Announcement> items = pageResult.getItems();
        for (Announcement item : items) {
            AnnouncementVo vo = new AnnouncementVo();
            BeanUtils.copyProperties(item, vo);
            voList.add(vo);
        }
        voPageResult.setItems(voList);
        //返回
        return ResponseEntity.ok(voPageResult);
    }
}

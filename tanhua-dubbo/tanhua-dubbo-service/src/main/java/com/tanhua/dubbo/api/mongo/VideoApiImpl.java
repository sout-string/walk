package com.tanhua.dubbo.api.mongo;

import com.tanhua.domain.mongo.FollowUser;
import com.tanhua.domain.mongo.Video;
import com.tanhua.domain.vo.PageResult;
import com.tanhua.dubbo.utils.IdService;
import org.apache.dubbo.config.annotation.Service;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * @author : TuGen
 * @date : 2021/5/14 20:58
 */
@Service
public class VideoApiImpl implements VideoApi {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private IdService idService;

    /**
     * 保存视频至mongo
     * @param video
     */
    @Override
    public void save(Video video) {
        video.setId(ObjectId.get());
        video.setCreated(System.currentTimeMillis());
        video.setVid(idService.nextId("video"));
        mongoTemplate.save(video);
    }

    /**
     * 查询视频
     * @param page
     * @param pagesize
     * @return
     */
    @Override
    public PageResult findPage(int page, int pagesize) {
        PageResult result = new PageResult();
        result.setPagesize((long) pagesize);
        result.setPage((long) page);
        Query query = new Query();
        long total = mongoTemplate.count(query, Video.class);
        result.setCounts(total);
        result.setPages(total % pagesize > 0 ? total / pagesize + 1 : total / pagesize);
        query.with(Sort.by(Sort.Order.desc("created")));
        query.limit(pagesize).skip((long) (page - 1) * pagesize);
        List<Video> items = mongoTemplate.find(query, Video.class);
        result.setItems(items);
        return result;
    }

    /**
     * 保存关注视频作者
     * @param followUser
     */
    @Override
    public void followUser(FollowUser followUser) {
        //补全创建时间
        followUser.setCreated(System.currentTimeMillis());
        //保存至mongo
        mongoTemplate.insert(followUser);
    }

    /**
     * 取消关注视频作者
     * @param followUser
     */
    @Override
    public void unfollowUser(FollowUser followUser) {
        //创建查询对象
        Query query = new Query();
        query.addCriteria(Criteria.where("followUserId").is(followUser.getFollowUserId()).and("userId").is(followUser.getUserId()));
        //删除
        mongoTemplate.remove(query, FollowUser.class);
    }
}

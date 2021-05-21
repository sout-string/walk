package com.tanhua.dubbo.api.mongo;

import com.tanhua.domain.mongo.Friend;
import com.tanhua.domain.mongo.Publish;
import com.tanhua.domain.mongo.RecommendQuanzi;
import com.tanhua.domain.mongo.TimeLine;
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
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : TuGen
 * @date : 2021/5/11 20:10
 */
@Service
public class PublishApiImpl implements PublishApi {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private IdService idService;
    private String collectionName;
    private TimeLine timeLine = new TimeLine();

    /**
     * 发布动态
     *
     * @param publish
     */
    @Override
    public void add(Publish publish) {
        //查询登录用户id
        Long loginUserId = publish.getUserId();
        //补全动态信息
        publish = createPublish(publish);
        //将动态存入动态表
        mongoTemplate.insert(publish);
        //查询好友信息
        List<Friend> friends = findFriends(loginUserId);
        //将动态存入好友的时间线表
        if (!CollectionUtils.isEmpty(friends)) {
            for (Friend friend : friends) {
                saveInTimeLine(publish, friend.getFriendId());
            }
        }
        //将动态存入自己的时间线表
        saveInTimeLine(publish, loginUserId);
    }

    /**
     * 根据id查询好友动态
     *
     * @param page
     * @param pagesize
     * @return
     */
    @Override
    public PageResult queryFriendPublishList(long page, long pagesize, Long userId) {
        //分页查询时间线表
        collectionName = "quanzi_time_line_" + userId;
        Query query = new Query();
        long total = mongoTemplate.count(query, collectionName);
        List<Publish> publishes = new ArrayList<>();
        if (total > 0) {
            query.with(Sort.by(Sort.Order.desc("created"))).with(PageRequest.of(Integer.parseInt(Long.toString(page - 1)), Integer.parseInt(Long.toString(pagesize))));
            List<TimeLine> timeLines = mongoTemplate.find(query, TimeLine.class, collectionName);
            //按时间线表信息查询好友动态
            List<ObjectId> publishIds = timeLines.stream().map(TimeLine::getPublishId).collect(Collectors.toList());
            Query publishQuery = new Query();
            publishQuery.addCriteria(Criteria.where("_id").in(publishIds));
            publishes = mongoTemplate.find(publishQuery, Publish.class);
        }

        //封装返回
        PageResult result = new PageResult();
        long pages = total / pagesize;
        if (total % pagesize > 0) {
            pages += 1;
        }
        result.setCounts(total);
        result.setPages(pages);
        result.setPage(page);
        result.setPagesize(pagesize);
        result.setItems(publishes);
        return result;
    }

    /**
     * 查询推荐动态
     * @param page
     * @param pagesize
     * @param userId
     * @return
     */
    @Override
    public PageResult findRecommendPublish(int page, int pagesize, Long userId) {
        //查询推荐表
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        query.with(Sort.by(Sort.Order.desc(("created"))));
        query.limit(pagesize).skip((page - 1) * pagesize);
        List<RecommendQuanzi> recommends = mongoTemplate.find(query, RecommendQuanzi.class);
        long total = mongoTemplate.count(query, RecommendQuanzi.class);
        //通过推荐表查询动态表
        List<Publish> list = new ArrayList<>();
        for (RecommendQuanzi recommend : recommends) {
            Publish publish = mongoTemplate.findById(recommend.getPublishId(), Publish.class);
            if (publish != null) {
                list.add(publish);
            }
        }
        //封装返回结果
        long pages = total % pagesize > 0 ? (total / pagesize + 1) : (total / pagesize);
        PageResult result = new PageResult();
        result.setPages(pages);
        result.setItems(list);
        result.setPagesize((long)pagesize);
        result.setPage((long) page);
        result.setCounts(total);
        return result;
    }

    /**
     * 根据用户id分页查询所有动态
     * @param page
     * @param pagesize
     * @param userId
     * @return
     */
    @Override
    public PageResult findAllPublish(int page, int pagesize, Long userId) {
        PageResult result = queryUserPublish(page, pagesize, userId);
        return result;
    }

    /**
     * 根据动态id查询动态
     * @param publishId
     * @return
     */
    @Override
    public Publish findById(String publishId) {
        Publish publish = mongoTemplate.findById(new ObjectId(publishId), Publish.class);
        return publish;
    }

    /**
     * 根据用户id查询动态
     * @param userId
     * @return
     */
    public PageResult queryUserPublish(long page, long pagesize, Long userId) {
        //根据用户id分页倒序查询动态表
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        long total = mongoTemplate.count(query, Publish.class);
        query.with(Sort.by(Sort.Order.desc("created"))).with(PageRequest.of(Integer.parseInt(Long.toString(page - 1)), Integer.parseInt(Long.toString(pagesize))));
        List<Publish> publishes = mongoTemplate.find(query, Publish.class);
        //封装返回
        PageResult result = new PageResult();
        long pages = total / pagesize;
        if (total % pagesize > 0) {
            pages += 1;
        }
        result.setCounts(total);
        result.setPages(pages);
        result.setPage(page);
        result.setPagesize(pagesize);
        result.setItems(publishes);
        return result;
    }

    /**
     * 查询某个用户的所有好友
     *
     * @param userId
     * @return
     */
    private List<Friend> findFriends(long userId) {
        //查询好友信息
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        List<Friend> friends = mongoTemplate.find(query, Friend.class);
        return friends;
    }

    /**
     * 将动态保存至某个用户的时间线表
     */
    private void saveInTimeLine(Publish publish, long userId) {
        timeLine.setCreated(publish.getCreated());
        timeLine.setPublishId(publish.getId());
        timeLine.setUserId(publish.getUserId());
        collectionName = "quanzi_time_line_" + userId;
        mongoTemplate.insert(timeLine, collectionName);
        collectionName = null;
    }


    /**
     * 补全动态信息并返回
     *
     * @return
     */
    private Publish createPublish(Publish publish) {
        //查询登录用户id
        Long loginUserId = publish.getUserId();
        //制作时间戳
        long timeMillis = System.currentTimeMillis();
        //制作pid
        long pid = idService.nextId("quanzi_publish");
        //补全信息
        publish.setPid(pid);
        publish.setId(new ObjectId());
        publish.setCreated(timeMillis);
        publish.setUserId(loginUserId);
        return publish;
    }
}

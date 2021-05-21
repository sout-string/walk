package com.tanhua.dubbo.api;

import com.tanhua.domain.mongo.Friend;
import com.tanhua.domain.mongo.RecommendUser;
import com.tanhua.domain.mongo.UserLike;
import com.tanhua.domain.mongo.Visitor;
import com.tanhua.domain.vo.PageResult;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : TuGen
 * @date : 2021/5/18 18:22
 */
@Service
public class UserLikeApiImpl implements UserLikeApi {
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 统计喜欢
     *
     * @param loginUserId
     * @return
     */
    @Override
    public Long countLike(Long loginUserId) {
        Query query = new Query(Criteria.where("userId").is(loginUserId));
        return mongoTemplate.count(query, UserLike.class);
    }

    /**
     * 统计粉丝
     *
     * @param loginUserId
     * @return
     */
    @Override
    public Long countFans(Long loginUserId) {
        Query query = new Query(Criteria.where("likeUserId").is(loginUserId));
        return mongoTemplate.count(query, UserLike.class);
    }

    /**
     * 统计好友
     *
     * @param loginUserId
     * @return
     */
    @Override
    public Long countLikeEachOther(Long loginUserId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(loginUserId));
        return mongoTemplate.count(query, Friend.class);
    }

    /**
     * 分页查询好友
     *
     * @param loginUserId
     * @param page
     * @param pagesize
     * @return
     */
    @Override
    public PageResult findPageLikeEachOther(Long loginUserId, int page, int pagesize) {
        PageResult result = new PageResult();
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(loginUserId));
        long total = mongoTemplate.count(query, Friend.class);
        //有无好友判断
        if (total > 0) {
            query.with(Sort.by(Sort.Order.desc("created")));
            query.limit(pagesize).skip((page - 1) * pagesize);
            List<Friend> friendList = mongoTemplate.find(query, Friend.class);
            //转换
            List<RecommendUser> list = new ArrayList<>();
            for (Friend friend : friendList) {
                //调用封装的查询、转换方法
                list.add(querySource(friend.getFriendId(), loginUserId));
            }
            result.setItems(list);
        }
        result.setCounts(total);
        result.setPage((long) page);
        result.setPagesize((long) pagesize);
        result.setPages(total % pagesize > 0 ? (total / pagesize + 1) : total / pagesize);
        return result;
    }


    /**
     * 分页查询喜欢
     *
     * @param loginUserId
     * @param page
     * @param pagesize
     * @return
     */
    @Override
    public PageResult findPageOneSideLike(Long loginUserId, int page, int pagesize) {
        PageResult<RecommendUser> result = new PageResult<>();
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(loginUserId));
        long total = mongoTemplate.count(query, UserLike.class);
        if (total > 0) {
            query.limit(pagesize).skip((page - 1) * pagesize);
            List<UserLike> userLikeList = mongoTemplate.find(query, UserLike.class);
            List<RecommendUser> list = new ArrayList<>();
            //转换
            for (UserLike userLike : userLikeList) {
                list.add(querySource(userLike.getLikeUserId(), loginUserId));
            }
            result.setItems(list);
        }
        result.setCounts(total);
        result.setPage((long) page);
        result.setPagesize((long) pagesize);
        result.setPages(total % pagesize > 0 ? (total / pagesize + 1) : total / pagesize);
        return result;
    }

    /**
     * 分页查询粉丝
     *
     * @param loginUserId
     * @param page
     * @param pagesize
     * @return
     */
    @Override
    public PageResult findPageFans(Long loginUserId, int page, int pagesize) {
        PageResult result = new PageResult();
        Query query = new Query();
        query.addCriteria(Criteria.where("likeUserId").is(loginUserId));
        long total = mongoTemplate.count(query, UserLike.class);
        if (total > 0) {
            query.limit(pagesize).skip((page - 1) * pagesize);
            List<UserLike> userLikeList = mongoTemplate.find(query, UserLike.class);
            List<RecommendUser> list = new ArrayList<>();
            for (UserLike userLike : userLikeList) {
                list.add(querySource(userLike.getUserId(), loginUserId));
            }
            result.setItems(list);
        }
        result.setCounts(total);
        result.setPage((long) page);
        result.setPagesize((long) pagesize);
        result.setPages(total % pagesize > 0 ? (total / pagesize + 1) : total / pagesize);
        return result;
    }

    /**
     * 分页查询访客
     *
     * @param loginUserId
     * @param page
     * @param pagesize
     * @return
     */
    @Override
    public PageResult findPageMyVisitors(Long loginUserId, int page, int pagesize) {
        PageResult result = new PageResult();
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(loginUserId));
        long total = mongoTemplate.count(query, Visitor.class);
        if (total > 0) {
            query.limit(pagesize).skip((page - 1) * pagesize);
            List<Visitor> visitorList = mongoTemplate.find(query, Visitor.class);
            List<RecommendUser> list = new ArrayList<>();
            for (Visitor visitor : visitorList) {
                list.add(querySource(visitor.getVisitorUserId(), loginUserId));
            }
            result.setItems(list);
        }
        result.setCounts(total);
        result.setPage((long) page);
        result.setPagesize((long) pagesize);
        result.setPages(total % pagesize > 0 ? (total / pagesize + 1) : total / pagesize);
        return result;
    }

    /**
     * 转换为推荐用户对象
     *
     * @param targetId
     * @param loginUserId
     * @return
     */
    private RecommendUser querySource(Long targetId, Long loginUserId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("toUserId").is(loginUserId).and("userId").is(targetId));
        query.with(Sort.by(Sort.Order.desc("date")));
        RecommendUser recommendUser = mongoTemplate.findOne(query, RecommendUser.class);
        //如果没有,创建默认值
        if (recommendUser == null) {
            recommendUser = new RecommendUser();
            recommendUser.setUserId(targetId);
            recommendUser.setToUserId(loginUserId);
            recommendUser.setScore(70d);
        }
        return recommendUser;
    }
}

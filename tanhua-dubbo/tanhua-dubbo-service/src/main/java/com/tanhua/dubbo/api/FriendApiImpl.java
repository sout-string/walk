package com.tanhua.dubbo.api;

import com.tanhua.domain.db.User;
import com.tanhua.domain.mongo.Friend;
import com.tanhua.domain.vo.PageResult;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * @author : TuGen
 * @date : 2021/5/16 21:33
 */
@Service
public class FriendApiImpl implements FriendApi {
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 保存好友关系
     * @param loginUserId
     * @param friendId
     */
    @Override
    public void add(Long loginUserId, Long friendId) {
        //查询是否已经是好友
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(loginUserId).and("friendId").is(friendId));
        if (!mongoTemplate.exists(query, Friend.class)) {
            //登录者不存在好友关系
            Friend friend = new Friend();
            friend.setFriendId(friendId);
            friend.setUserId(loginUserId);
            friend.setCreated(System.currentTimeMillis());
            mongoTemplate.insert(friend);
        }
        Query friendQuery = new Query();
        friendQuery.addCriteria(Criteria.where("userId").is(friendId).and("friendId").is(loginUserId));
        if (!mongoTemplate.exists(friendQuery,Friend.class)) {
            //目标不存在好友关系
            Friend friend = new Friend();
            friend.setFriendId(loginUserId);
            friend.setUserId(friendId);
            friend.setCreated(System.currentTimeMillis());
            mongoTemplate.insert(friend);
        }
    }

    /**
     * 分页查询好友
     * @param userId
     * @param page
     * @param pagesize
     * @param keyword
     * @return
     */
    @Override
    public PageResult findPage(Long userId, Integer page, Integer pagesize, String keyword) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        long total = mongoTemplate.count(query, Friend.class);
        query.limit(pagesize).skip((page - 1) * pagesize);
        List<Friend> friends = mongoTemplate.find(query, Friend.class);
        PageResult<Friend> result = new PageResult<>();
        result.setPage(Long.valueOf(page));
        result.setPagesize(Long.valueOf(pagesize));
        result.setCounts(total);
        result.setPages(total%pagesize>0?(total/pagesize+1):total/pagesize);
        result.setItems(friends);
        return result;
    }
}

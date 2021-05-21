package com.tanhua.dubbo.api;

import com.tanhua.domain.db.UserInfo;
import com.tanhua.domain.mongo.UserLike;
import com.tanhua.dubbo.mapper.UserInfoMapper;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;


/**
 * @author : TuGen
 * @date : 2021/5/7 10:29
 */
@Service
public class UserInfoApiImpl implements UserInfoApi {
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private FriendApi friendApi;

    @Override
    public void add(UserInfo userInfo) {
        userInfoMapper.insert(userInfo);
    }

    @Override
    public void update(UserInfo userInfo) {
        userInfoMapper.updateById(userInfo);
    }

    @Override
    public UserInfo findById(long userId) {
        return userInfoMapper.selectById(userId);
    }

    @Override
    public List<UserInfo> selectByIds(List<Long> blackUserIds) {
        return userInfoMapper.selectBatchIds(blackUserIds);
    }

    /**
     * 建立喜欢或好友关系
     * @param loginUserId
     * @param fansId
     * @return
     */
    @Override
    public Boolean like(Long loginUserId, Long fansId) {
        //先判断对方是否喜欢自己
        Query query = new Query();
        query.addCriteria(Criteria.where("likeUserId").is(loginUserId).and("userId").is(fansId));
        long count = mongoTemplate.count(query, UserLike.class);
        if (count > 0) {
            //说明对方是自己粉丝，先接触粉丝关系
            mongoTemplate.remove(query);
            //建立好友关系
            friendApi.add(loginUserId, fansId);
            return true;
        } else {
            //不是粉丝，建立关注关系
            UserLike userLike = new UserLike();
            userLike.setUserId(loginUserId);
            userLike.setLikeUserId(fansId);
            userLike.setCreated(System.currentTimeMillis());
            mongoTemplate.insert(userLike);
        }
        return false;
    }
}



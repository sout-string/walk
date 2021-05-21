package com.tanhua.dubbo.api;

import com.tanhua.domain.mongo.UserLocation;
import com.tanhua.domain.vo.UserLocationVo;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

/**
 * @author : TuGen
 * @date : 2021/5/19 15:28
 */
@Service
public class UserLocationApiImpl implements UserLocationApi {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Override
    public void addLocation(Double longitude, Double latitude, String addStr, Long userId) {
        long timeMillis = System.currentTimeMillis();
        //查询用户当前位置
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        if (mongoTemplate.exists(query, UserLocation.class)) {
            //已经记录了用户位置，更新
            Update update = new Update();
            update.set("lastUpdated", timeMillis);
            update.set("update", timeMillis);
            update.set("location", new GeoJsonPoint(longitude, latitude));
            update.set("address", addStr);
            mongoTemplate.updateFirst(query, update, UserLocation.class);
        } else {
            UserLocation userLocation = new UserLocation();
            userLocation.setLocation(new GeoJsonPoint(longitude, latitude));
            userLocation.setCreated(timeMillis);
            userLocation.setUserId(userId);
            userLocation.setAddress(addStr);
            mongoTemplate.insert(userLocation);
        }
    }

    /**
     * 搜索附近用户
     * @param loginUserId
     * @param miles
     * @return
     */
    @Override
    public List<UserLocationVo> searchNear(Long loginUserId, String miles) {
        //查询登录用户位置
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(loginUserId));
        UserLocation userLocation = mongoTemplate.findOne(query, UserLocation.class);
        //指定查询半径
        GeoJsonPoint location = userLocation.getLocation();
        Distance distance = new Distance(Long.valueOf(miles) / 1000, Metrics.KILOMETERS);
        //根据此半径画圆
        Circle circle = new Circle(location, distance);
        //查询
        Query nearQuery = new Query();
        nearQuery.addCriteria(Criteria.where("location").withinSphere(circle));
        List<UserLocation> userLocations = mongoTemplate.find(nearQuery, UserLocation.class);
        //转化
        List<UserLocationVo> userLocationVos = UserLocationVo.formatToList(userLocations);
        return userLocationVos;
    }
}

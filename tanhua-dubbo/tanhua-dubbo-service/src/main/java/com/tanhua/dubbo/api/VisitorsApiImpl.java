package com.tanhua.dubbo.api;

import com.tanhua.domain.mongo.RecommendUser;
import com.tanhua.domain.mongo.Visitor;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;



import java.util.List;

/**
 * @author : TuGen
 * @date : 2021/5/18 17:49
 */
@Service
public class VisitorsApiImpl implements VisitorsApi {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Override
    public List<Visitor> queryVisitors(Long loginUserId, String lastTime) {
        //查询mongo
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(loginUserId));
        if (StringUtils.isNotEmpty(lastTime)) {
            Long date = Long.valueOf(lastTime);
            query.addCriteria(Criteria.where("date").gte(date));
        }
        query.with(Sort.by(Sort.Order.desc("date")));
        query.limit(5);
        List<Visitor> visitorList = mongoTemplate.find(query, Visitor.class);
        //查询访客缘分值
        if (CollectionUtils.isNotEmpty(visitorList)) {
            for (Visitor visitor : visitorList) {
                Long visitorUserId = visitor.getVisitorUserId();
                Query recommendUserQuery = new Query();
                recommendUserQuery.addCriteria(Criteria.where("toUserId").is(loginUserId).and("userId").is(visitorUserId));
                RecommendUser recommendUser = mongoTemplate.findOne(recommendUserQuery, RecommendUser.class);
                if (recommendUser == null) {
                    visitor.setScore(70d);
                } else {
                    visitor.setScore(recommendUser.getScore());
                }
            }
        }
        return visitorList;
    }

    /**
     * 保存访客记录
     * @param visitor
     */
    @Override
    public void save(Visitor visitor) {
        visitor.setDate(System.currentTimeMillis());
        mongoTemplate.save(visitor);
    }
}

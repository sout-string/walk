package com.tanhua.dubbo.api.mongo;

import com.tanhua.domain.mongo.RecommendUser;
import com.tanhua.domain.vo.PageResult;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * @author : TuGen
 * @date : 2021/5/10 16:27
 */
@Service
public class RecommendUserApiImpl implements RecommendUserApi {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public RecommendUser queryTodayBest(Long userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("touserId").is(userId));
        query.with(Sort.by(Sort.Order.desc("score"))).limit(1);
        RecommendUser beauty = mongoTemplate.findOne(query, RecommendUser.class);
        return beauty;
    }

    /**
     * 分页查询首页推荐
     * @param page
     * @param pageSize
     * @param userId
     * @return
     */
    @Override
    public PageResult findPage(Integer page, Integer pageSize, Long userId) {
        Query query = new Query();
        //设置查询条件
        query.addCriteria(Criteria.where("tousrtId").is(userId));
        //查询总数
        long total = mongoTemplate.count(query, RecommendUser.class);
        //分页查询参数设置
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Order.desc("score")));
        //分页查询
        query.with(pageRequest);
        List<RecommendUser> recommendUserList = mongoTemplate.find(query, RecommendUser.class);
        //封装结果
        PageResult<RecommendUser> pageResult = new PageResult<>();
        pageResult.setItems(recommendUserList);
        pageResult.setCounts(total);
        pageResult.setPage(Long.valueOf(page));
        pageResult.setPagesize(Long.valueOf(pageSize));
        //计算封装总页数
        long pages = total / pageSize;
        pageResult.setPages(pages);
        return pageResult;
    }

    /**
     * 查询缘分值
     * @param userId
     * @param toUserId
     * @return
     */
    @Override
    public Double queryForScore(Long userId, Long toUserId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("toUserId").is(toUserId).and("userId").is(userId));
        query.with(Sort.by(Sort.Order.desc("date")));
        RecommendUser bestUser = mongoTemplate.findOne(query, RecommendUser.class);
        if (bestUser == null) {
            return 95d;
        }
        return bestUser.getScore();
    }
}

package com.tanhua.dubbo.api.mongo;

import com.tanhua.domain.mongo.RecommendUser;
import com.tanhua.domain.vo.PageResult;
import org.springframework.stereotype.Service;


/**
 * @author : TuGen
 * @date : 2021/5/10 16:26
 */
public interface RecommendUserApi {
    RecommendUser queryTodayBest(Long userId);

    PageResult findPage(Integer page, Integer pageSize, Long userId);

    Double queryForScore(Long userId, Long toUserId);
}

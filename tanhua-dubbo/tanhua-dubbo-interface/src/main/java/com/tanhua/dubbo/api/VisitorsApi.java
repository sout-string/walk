package com.tanhua.dubbo.api;

import com.tanhua.domain.mongo.Visitor;

import java.util.List;

/**
 * @author : TuGen
 * @date : 2021/5/18 17:48
 */
public interface VisitorsApi {
    List<Visitor> queryVisitors(Long loginUserId, String lastTime);

    void save(Visitor visitor);
}

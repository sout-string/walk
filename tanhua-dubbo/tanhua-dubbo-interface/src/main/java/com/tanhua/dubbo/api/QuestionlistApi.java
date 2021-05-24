package com.tanhua.dubbo.api;

import com.tanhua.domain.db.Questionlist;

import java.util.List;

/**
 * @description
 * @author:Dell
 * @date:2021/5/23 11:13
 */
public interface QuestionlistApi {


    List<Questionlist> findAllTable();
}

package com.tanhua.dubbo.api.mongo;

import com.tanhua.domain.mongo.Comment;
import com.tanhua.domain.vo.PageResult;

/**
 * @author : TuGen
 * @date : 2021/5/13 17:07
 */
public interface CommentApi {
    long save(Comment comment);

    long delete(Comment comment);

    PageResult findPage(String publishId, Long page, Long pagesize);

    PageResult findByUserId(Integer page, Integer pagesize, Integer commentType, Long userId);
}

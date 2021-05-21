package com.tanhua.dubbo.api;

import com.tanhua.domain.db.UserInfo;

import java.util.List;

/**
 * @author : TuGen
 * @date : 2021/5/7 10:25
 */
public interface UserInfoApi {
    /**
     * 添加方法
     * @param userInfo
     */
    void add(UserInfo userInfo);

    /**
     * 更新方法
     * @param userInfo
     */
    void update(UserInfo userInfo);

    /**
     * 根据id查询用户信息
     *
     * @param userId
     * @return
     */
    UserInfo findById(long userId);

    List<UserInfo> selectByIds(List<Long> blackUserIds);

    Boolean like(Long loginUserId, Long fansId);
}

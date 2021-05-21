package com.tanhua.server.service;

import com.tanhua.domain.db.UserInfo;
import com.tanhua.domain.vo.RecommendUserVo;
import com.tanhua.domain.vo.TodayBestVo;
import com.tanhua.dubbo.api.UserInfoApi;
import com.tanhua.dubbo.api.mongo.RecommendUserApi;
import com.tanhua.server.interceptor.UserHolder;
import com.tanhua.domain.mongo.RecommendUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * @author : TuGen
 * @date : 2021/5/10 16:23
 */
@Service
@Slf4j
public class TodayBestService {
    @Reference
    private UserInfoApi userInfoApi;
    @Reference
    private RecommendUserApi recommendUserApi;

    /**
     * 查看今日最佳
     *
     * @return
     */
    public TodayBestVo queryTodayBest() {
        log.info("进入了今日佳人");
        //获取用户信息
        Long userId = UserHolder.getUserId();
        //获取今日最佳
        RecommendUser recommendUser = recommendUserApi.queryTodayBest(userId);
        //判断获取结果
        if (recommendUser == null) {
            //无，创建默认今日佳人
            recommendUser = new RecommendUser();
            recommendUser.setScore(95d);
            recommendUser.setUserId(51L);
        }
        UserInfo user = userInfoApi.findById(recommendUser.getUserId());
        TodayBestVo todayBestVo = new TodayBestVo();
        BeanUtils.copyProperties(user, todayBestVo);
        todayBestVo.setId(user.getId().intValue());
        todayBestVo.setTags(StringUtils.split(user.getTags(), ","));
        todayBestVo.setFateValue((int) recommendUser.getScore().longValue());
        //返回今日佳人
        return todayBestVo;
    }


}

package com.tanhua.dubbo.api;

import com.tanhua.domain.vo.UserLocationVo;

import java.util.List;

/**
 * @author : TuGen
 * @date : 2021/5/19 15:28
 */
public interface UserLocationApi {
    void addLocation(Double longitude, Double latitude, String addStr,Long userId);

    List<UserLocationVo> searchNear(Long loginUserId, String distance);
}

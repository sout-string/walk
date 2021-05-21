package com.tanhua.server.service;

import com.tanhua.domain.db.UserInfo;
import com.tanhua.domain.vo.NearUserVo;
import com.tanhua.domain.vo.UserLocationVo;
import com.tanhua.dubbo.api.UserInfoApi;
import com.tanhua.dubbo.api.UserLocationApi;
import com.tanhua.server.interceptor.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author : TuGen
 * @date : 2021/5/19 15:20
 */
@Service
@Slf4j
public class LocationService {
    @Reference
    private UserLocationApi userLocationApi;
    @Reference
    private UserInfoApi userInfoApi;

    /**
     * 上报地理位置
     * @param paramMap
     */
    public void reportLocation(Map<String, Object> paramMap) {
        Double longitude = (Double) paramMap.get("longitude");
        Double latitude = (Double) paramMap.get("latitude");
        String addStr = (String) paramMap.get("addStr");
        log.info("上传的经纬度是:"+longitude+","+latitude);
        Long userId = UserHolder.getUserId();
        userLocationApi.addLocation(longitude, latitude, addStr, userId);
    }

    /**
     * 查找附近用户
     * @param gender
     * @param distance
     * @return
     */
    public List<NearUserVo> searchNearBy(String gender, String distance) {
        Long loginUserId = UserHolder.getUserId();
        //查询附近用户
        List<UserLocationVo> locations = userLocationApi.searchNear(loginUserId, distance);
        //查询用户数据并补全
        List<NearUserVo> list = new ArrayList<>();
        for (UserLocationVo location : locations) {
            //排除用户本身
            if (loginUserId.equals(location.getUserId())) {
                continue;
            }
            UserInfo userInfo = userInfoApi.findById(location.getUserId());
            //性别排除
            if (gender!=null && !userInfo.getGender().equals(gender)) {
                continue;
            }
            NearUserVo vo = new NearUserVo();
            BeanUtils.copyProperties(userInfo, vo);
            vo.setAvatar(userInfo.getAvatar());
            vo.setUserId(userInfo.getId());
            vo.setNickname(userInfo.getNickname());
            list.add(vo);
        }
        return list;
    }


}

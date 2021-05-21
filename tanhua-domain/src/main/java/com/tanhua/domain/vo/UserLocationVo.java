package com.tanhua.domain.vo;

import com.tanhua.domain.mongo.UserLocation;
import lombok.Data;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserLocationVo implements Serializable {
    private String id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 经度
     */
    private Double longitude;
    /**
     * 维度
     */
    private Double latitude;
    /**
     * 位置描述
     */
    private String address;
    /**
     * 创建时间
     */
    private Long created;
    /**
     * 更新时间
     */
    private Long updated;
    /**
     * 上次更新时间
     */
    private Long lastUpdated;

    public static final UserLocationVo format(UserLocation userLocation) {
        UserLocationVo userLocationVo = new UserLocationVo();
        userLocationVo.setAddress(userLocation.getAddress());
        userLocationVo.setCreated(userLocation.getCreated());
        userLocationVo.setId(userLocation.getId().toHexString());
        userLocationVo.setLastUpdated(userLocation.getLastUpdated());
        userLocationVo.setUpdated(userLocation.getUpdated());
        userLocationVo.setUserId(userLocation.getUserId());
        userLocationVo.setLongitude(userLocation.getLocation().getX());
        userLocationVo.setLatitude(userLocation.getLocation().getY());
        return userLocationVo;
    }

    public static final List<UserLocationVo> formatToList(List<UserLocation> userLocations) {
        List<UserLocationVo> list = new ArrayList<>();
        for (UserLocation userLocation : userLocations) {
            list.add(format(userLocation));
        }
        return list;
    }
}
package com.tanhua.server.service;

import com.github.tobato.fastdfs.domain.conn.FdfsWebServer;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.tanhua.commons.templates.OssTemplate;
import com.tanhua.domain.db.UserInfo;
import com.tanhua.domain.mongo.FollowUser;
import com.tanhua.domain.mongo.Video;
import com.tanhua.domain.vo.PageResult;
import com.tanhua.domain.vo.VideoVo;
import com.tanhua.dubbo.api.UserInfoApi;
import com.tanhua.dubbo.api.mongo.VideoApi;
import com.tanhua.server.interceptor.UserHolder;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : TuGen
 * @date : 2021/5/14 20:38
 */
@Service
public class VideoService {
    @Reference
    private VideoApi videoApi;
    @Reference
    private UserInfoApi userInfoApi;
    @Autowired
    private FastFileStorageClient fileStorageClient;
    @Autowired
    private FdfsWebServer fdfsWebServer;
    @Autowired
    private OssTemplate ossTemplate;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 发布小视频
     *
     * @param videoThumbnail
     * @param videoFile
     */
    public void post(MultipartFile videoThumbnail, MultipartFile videoFile) throws IOException {
        //上传视频图片，获取地址
        String picUrl = ossTemplate.upload(videoThumbnail.getOriginalFilename(), videoThumbnail.getInputStream());
        //获得视频名
        String videoFileOriginalFilename = videoFile.getOriginalFilename();
        //截取后缀
        String suffix = videoFileOriginalFilename.substring(videoFileOriginalFilename.lastIndexOf("."));
        //上传图片
        StorePath storePath = fileStorageClient.uploadFile(videoFile.getInputStream(), videoFile.getSize(), suffix, null);
        //获取视频地址
        String videoUrl = fdfsWebServer.getWebServerUrl() + storePath.getFullPath();
        //构建video对象
        Video video = new Video();
        video.setPicUrl(picUrl);
        video.setVideoUrl(videoUrl);
        video.setText(videoFileOriginalFilename.substring(0, videoFileOriginalFilename.lastIndexOf(".")));
        video.setUserId(UserHolder.getUserId());
        //保存video对象
        videoApi.save(video);
    }

    /**
     * 分页查询视频
     *
     * @param page
     * @param pagesize
     * @return
     */
    public PageResult<VideoVo> findPage(int page, int pagesize) {
        //查询
        PageResult result = videoApi.findPage(page, pagesize);
        //转化
        List<Video> items = result.getItems();
        List<VideoVo> list = new ArrayList<>();
        String key;
        for (Video video : items) {
            UserInfo userInfo = userInfoApi.findById(UserHolder.getUserId());
            VideoVo vo = new VideoVo();
            BeanUtils.copyProperties(video, vo);
            BeanUtils.copyProperties(userInfo, vo);
            vo.setCover(video.getPicUrl());
            vo.setId(video.getId().toHexString());
            if (StringUtils.isNotEmpty(video.getText())) {
                vo.setSignature(video.getText());
            } else {
                //默认用户昵称做签名
                vo.setSignature(userInfoApi.findById(video.getUserId()).getNickname());
            }
            vo.setHasLiked(0);
            vo.setHasFocus(0);
            key = "publish_like_" + UserHolder.getUserId() + "_" + video.getId();
            if (redisTemplate.hasKey(key)) {
                vo.setHasLiked(1);
            }
            key = "publish_focus_" + UserHolder.getUserId() + "_" + video.getId();
            if (redisTemplate.hasKey(key)) {
                vo.setHasFocus(1);
            }
            list.add(vo);
        }
        result.setItems(list);
        return result;
    }

    /**
     * 关注视频的作者
     * @param userId
     */
    public void followUser(long userId) {
        //构建关注对象
        FollowUser followUser = new FollowUser();
        followUser.setUserId(UserHolder.getUserId());
        followUser.setFollowUserId(userId);
        //保存记录
        videoApi.followUser(followUser);
        String key = "video_follow_" + UserHolder.getUserId() + "_" + userId;
        redisTemplate.opsForValue().set(key, "1");
    }

    /**
     * 取消关注
     *
     * @param userId
     */
    public void unfollowUser(long userId) {
        //构建关注对象
        FollowUser followUser = new FollowUser();
        followUser.setUserId(UserHolder.getUserId());
        followUser.setFollowUserId(userId);
        //取消保存记录
        videoApi.unfollowUser(followUser);
        String key = "video_follow_" + UserHolder.getUserId() + "_" + userId;
        redisTemplate.delete(key);
    }
}

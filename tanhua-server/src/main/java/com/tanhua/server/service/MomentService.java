package com.tanhua.server.service;

import com.tanhua.commons.templates.OssTemplate;
import com.tanhua.domain.db.UserInfo;
import com.tanhua.domain.exception.TanHuaException;
import com.tanhua.domain.mongo.Comment;
import com.tanhua.domain.mongo.Publish;
import com.tanhua.domain.mongo.Visitor;
import com.tanhua.domain.vo.MomentVo;
import com.tanhua.domain.vo.PageResult;
import com.tanhua.domain.vo.PublishVo;
import com.tanhua.domain.vo.VisitorVo;
import com.tanhua.dubbo.api.VisitorsApi;
import com.tanhua.dubbo.api.mongo.PublishApi;
import com.tanhua.dubbo.api.UserInfoApi;
import com.tanhua.server.interceptor.UserHolder;
import com.tanhua.server.utils.RelativeDateFormat;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.split;

/**
 * @author : TuGen
 * @date : 2021/5/11 19:57
 */
@Service
@Slf4j
public class MomentService {
    @Reference
    private PublishApi publishApi;
    @Autowired
    private OssTemplate ossTemplate;
    @Reference
    private UserInfoApi userInfoApi;
    @Autowired
    private RedisTemplate redisTemplate;
    @Reference
    private VisitorsApi visitorsApi;


    /**
     * 发布动态
     *
     * @param vo
     * @param imageContent
     */
    public void postMoment(PublishVo vo, MultipartFile[] imageContent) {
        //获取登录用户id
        Long loginUserId = UserHolder.getUserId();
        //将上传的图片存入oss
        List<String> medias = new ArrayList<>();
        for (MultipartFile image : imageContent) {
            try {
                String originalFilename = image.getOriginalFilename();
                InputStream inputStream = image.getInputStream();
                String url = ossTemplate.upload(originalFilename, inputStream);
                medias.add(url);
            } catch (IOException e) {
                e.printStackTrace();
                log.error("动态中图片上传失败");
                throw new TanHuaException("上传动态图片失败");
            }
        }
        //将vo、imageContent转化为动态对象
        Publish publish = new Publish();
        BeanUtils.copyProperties(vo, publish);
        publish.setMedias(medias);
        //设置作者id
        publish.setUserId(loginUserId);
        //设置状态
        publish.setState(0);
        publish.setSeeType(1);
        //将动态对象存入
        publishApi.add(publish);
    }

    /**
     * 分页查询所有好友动态
     *
     * @param page
     * @param pagesize
     * @return
     */
    public PageResult<MomentVo> queryFriendPublishList(long page, long pagesize) {
        Long loginUserId = UserHolder.getUserId();
        //根据用户id，查询动态
        PageResult result = publishApi.queryFriendPublishList(page, pagesize,loginUserId);
      /*  //转化
        List<Publish> publishes = result.getItems();
        List<MomentVo> momentVos = new ArrayList<>();
        for (Publish publish : publishes) {
            MomentVo momentVo = new MomentVo();
            //转化publish
            BeanUtils.copyProperties(publish, momentVo);
            //查询好友用户信息
            UserInfo friendUserInfo = userInfoApi.findById(publish.getUserId());
            //转化好友信息
            BeanUtils.copyProperties(friendUserInfo, momentVo);
            //补全信息
            momentVo.setImageContent(publish.getMedias().toArray(new String[]{}));
            momentVo.setCreateDate(RelativeDateFormat.format(new Date(publish.getCreated())));
            momentVo.setId(publish.getId().toString());
            momentVo.setTags(split(friendUserInfo.getTags(),","));
            momentVo.setDistance("1公里");
            momentVo.setHasLiked(0);
            momentVo.setHasLoved(0);
            //存入集合
            momentVos.add(momentVo);
        }
        result.setItems(momentVos);*/
        //返回结果
        return transformToMomentVo(result);
    }

    public PageResult<MomentVo> queryRecommendPublishList(int page, int pagesize) {
        //获取登录用户id
        Long loginUserId = UserHolder.getUserId();
        //查询推荐表
        PageResult result = publishApi.findRecommendPublish(page, pagesize, loginUserId);
        return transformToMomentVo(result);
    }

    /**
     * 分页查询动态结果转化为前端接收的结果
     * @param result
     * @return
     */
    private PageResult<MomentVo> transformToMomentVo(PageResult result) {
        //通过推荐表查询动态并构建结果
        List<Publish> items = result.getItems();
        List<MomentVo> list = new ArrayList<>();
        if (items != null) {
            for (Publish item : items) {
                //后期补充在API中拉取所以评论集子评论
                //创建前端动态对象
                MomentVo momentVo = new MomentVo();
                //查询作者id
                Long userId = item.getUserId();
                //查询作者信息
                UserInfo userInfo = userInfoApi.findById(userId);
                //添加作者信息
                if (userInfo != null) {
                    BeanUtils.copyProperties(userInfo, momentVo);
                    if (userInfo.getTags() != null) {
                        momentVo.setTags(split(userInfo.getTags(),","));
                    }
                }
                //添加动态信息
                BeanUtils.copyProperties(item, momentVo);
                momentVo.setId(item.getId().toHexString());
                momentVo.setCreateDate(RelativeDateFormat.format(new Date(item.getCreated())));
                momentVo.setHasLiked(0);
                //查询登录用户是否点赞
                String key = "publish_like_" + UserHolder.getUserId() + "_" + momentVo.getId();
                if (redisTemplate.hasKey(key)) {
                    momentVo.setHasLiked(1);
                }
                momentVo.setHasLoved(0);
                //查询登录用户是否喜欢
                key = "publish_love_" + UserHolder.getUserId() + "_" + momentVo.getId();
                if (redisTemplate.hasKey(key)) {
                    momentVo.setHasLoved(1);
                }
                momentVo.setImageContent(item.getMedias().toArray(new String[]{}));
                momentVo.setDistance("50米");
                //存入
                list.add(momentVo);
            }
        }
        result.setItems(list);
        //返回结果
        return result;
    }

    /**
     * 分页查询用户所有动态
     * @param page
     * @param pagesize
     * @param userId
     * @return
     */
    public PageResult<MomentVo> findAllPublishList(int page, int pagesize, Long userId) {
        if (userId == null) {
            userId = UserHolder.getUserId();
        }
        PageResult result = publishApi.findAllPublish(page, pagesize, userId);
        return transformToMomentVo(result);
    }

    /**
     * 查询单挑动态信息
     * @param publishId
     * @return
     */
    public MomentVo findById(String publishId) {
        Publish publish = publishApi.findById(publishId);
        //封装成怕个result，交给转换方法去补全
        PageResult result = new PageResult();
        List<Publish> list = new ArrayList<>();
        list.add(publish);
        result.setItems(list);
        PageResult<MomentVo> result1 = transformToMomentVo(result);
        return result1.getItems().get(0);
    }

    /**
     * 谁看过我
     * @return
     */
    public List<VisitorVo> queryVisitors() {
        Long loginUserId = UserHolder.getUserId();
        //获取redis中上一次登录时间
        String key = "visitors_time_" + loginUserId;
        String lastTime = (String) redisTemplate.opsForValue().get(key);
        //查询
        List<Visitor> visitorList = visitorsApi.queryVisitors(loginUserId, lastTime);
        List<VisitorVo> voList = new ArrayList<>();
        //转换
        if (!CollectionUtils.isEmpty(visitorList)) {
            for (Visitor visitor : visitorList) {
                VisitorVo vo = new VisitorVo();
                Long visitorUserId = visitor.getVisitorUserId();
                UserInfo visitorUserInfo = userInfoApi.findById(visitorUserId);
                BeanUtils.copyProperties(visitorUserInfo,vo);
                vo.setTags(StringUtils.split(visitorUserInfo.getTags(), ","));
                vo.setFateValue(visitor.getScore().intValue());
                voList.add(vo);
            }
        }
        //redis记录查询
        redisTemplate.opsForValue().set(key, System.currentTimeMillis() + "");
        //返回
        return voList;
    }
}

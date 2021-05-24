package com.tanhua.server.service;

import com.tanhua.domain.db.Question;
import com.tanhua.domain.db.UserInfo;
import com.tanhua.domain.mongo.RecommendUser;
import com.tanhua.domain.vo.PageResult;
import com.tanhua.domain.vo.RecommendUserQueryParam;
import com.tanhua.domain.vo.RecommendUserVo;
import com.tanhua.dubbo.api.QuestionApi;
import com.tanhua.dubbo.api.UserInfoApi;
import com.tanhua.dubbo.api.mongo.RecommendUserApi;
import com.tanhua.server.interceptor.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;

import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


import java.util.ArrayList;
import java.util.List;

/**
 * @author : TuGen
 * @date : 2021/5/10 20:25
 */
@Service
@Slf4j
public class RecommendUserService {
    @Reference
    private RecommendUserApi recommendUserApi;
    @Reference
    private UserInfoApi userInfoApi;
    @Reference
    private QuestionApi questionApi;


    /**
     * 分页查询首页推荐
     *
     * @param recommendUserQueryParam
     * @return
     */
    public PageResult<RecommendUserVo> recommendList(RecommendUserQueryParam recommendUserQueryParam) {
        log.info("用户" + userInfoApi.findById(UserHolder.getUserId()).getNickname() + "请求了首页推荐");
        //查找当前登录用户
        Long userId = UserHolder.getUserId();
        //查找首页推荐
        PageResult result = recommendUserApi.findPage(recommendUserQueryParam.getPage(), recommendUserQueryParam.getPagesize(), userId);

        List<RecommendUser> items = result.getItems();
        //无推荐使用默认推荐
        if (items == null || items.isEmpty()) {
            result = new PageResult(10L, recommendUserQueryParam.getPagesize().longValue(), 1L, 1L, null);
            items = defaultRecommend();
        }
        //转换
        List<RecommendUserVo> recommendUsers = new ArrayList<RecommendUserVo>();
        for (RecommendUser item : items) {
            RecommendUserVo recommendUserVo = new RecommendUserVo();
            BeanUtils.copyProperties(item, recommendUserVo);
            //补全用户信息
            UserInfo user = userInfoApi.findById(item.getUserId());
            recommendUserVo.setFateValue(item.getScore().longValue());
            recommendUserVo.setAvatar(user.getAvatar());
            recommendUserVo.setAge(user.getAge());
            recommendUserVo.setGender(user.getGender());
            recommendUserVo.setNickname(user.getNickname());
            recommendUserVo.setId(user.getId());
            recommendUserVo.setTags(StringUtils.split(user.getTags(), ","));
            recommendUsers.add(recommendUserVo);
        }
        result.setItems(recommendUsers);
        return result;
        //返回结果
    }



    /**
     * 创建默认数据
     */
    private List<RecommendUser> defaultRecommend() {
        String ids = "1,2,3,4,5,6,7,8,9,10";
        ArrayList<RecommendUser> records = new ArrayList<>();
        for (String id : ids.split(",")) {
            RecommendUser recommendUser = new RecommendUser();
            recommendUser.setUserId(Long.valueOf(id));
            recommendUser.setScore(RandomUtils.nextDouble(70, 98));
            records.add(recommendUser);
        }
        return records;
    }

    /**
     * 查询佳人信息
     * @param userId
     * @return
     */
    public RecommendUserVo getUserInfo(Long userId) {
        //查询用户信息
        UserInfo user = userInfoApi.findById(userId);
        //转换格式
        RecommendUserVo bestVo = new RecommendUserVo();
        BeanUtils.copyProperties(user, bestVo, "tags");
        //查询推荐分值
        Double score = recommendUserApi.queryForScore(userId, UserHolder.getUserId());
        if (score == null) {
            bestVo.setFateValue(80L);
        }
        bestVo.setFateValue(score.longValue());
        bestVo.setTags(StringUtils.split(user.getTags(), ","));
        return bestVo;
    }

    /**
     * 查询用户设置的陌生人问题
     * @param userId
     * @return
     */
    public String queryStrangerQuestions(Long userId) {
        Question question = questionApi.findByUserId(userId);
        if (question == null || StringUtils.isEmpty(question.getTxt())) {
            return "默认陌生人问题·你喜欢什么颜色？";
        }
        return question.getTxt();
    }
}

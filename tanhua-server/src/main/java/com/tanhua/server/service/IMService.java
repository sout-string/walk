package com.tanhua.server.service;

import com.alibaba.fastjson.JSON;
import com.tanhua.commons.templates.HuanXinTemplate;
import com.tanhua.domain.db.Question;
import com.tanhua.domain.db.UserInfo;
import com.tanhua.domain.mongo.Comment;
import com.tanhua.domain.mongo.Friend;
import com.tanhua.domain.vo.ContactVo;
import com.tanhua.domain.vo.MessageVo;
import com.tanhua.domain.vo.PageResult;
import com.tanhua.dubbo.api.FriendApi;
import com.tanhua.dubbo.api.QuestionApi;
import com.tanhua.dubbo.api.UserInfoApi;
import com.tanhua.dubbo.api.mongo.CommentApi;
import com.tanhua.server.interceptor.UserHolder;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author : TuGen
 * @date : 2021/5/16 20:58
 */
@Service
public class IMService {

    @Autowired
    private HuanXinTemplate huanXinTemplate;

    @Reference
    private QuestionApi questionApi;

    @Reference
    private UserInfoApi userInfoApi;

    @Reference
    private FriendApi friendApi;

    @Reference
    private CommentApi commentApi;
    /**
     * 回复陌生人消息
     *
     * @param paramMap
     */
    public void replyStrangerQuestions(Map<String, Object> paramMap) {
        //获取请求信息
        long userId = Long.parseLong(paramMap.get("userId").toString());
        String reply = (String) paramMap.get("reply");
        //查询登录用户信息与回复的问题
        UserInfo userInfo = userInfoApi.findById(UserHolder.getUserId());
        Question question = questionApi.findByUserId(userId);
        //构建消息内容
        HashMap<String, String> map = new HashMap<>();
        map.put("userId", userInfo.getId().toString());
        map.put("nickname", userInfo.getNickname());
        map.put("strangerQuestion", question == null ? "你喜欢什么颜色？" : question.getTxt());
        map.put("reply", reply);
        String msg = JSON.toJSONString(map);
        //发送消息给im
        huanXinTemplate.sendMsg(userId + "", msg);
    }

    /**
     * 添加联系人
     *
     * @param friendId
     */
    public void addContacts(Long friendId) {
        Long loginUserId = UserHolder.getUserId();
        //数据库添加好友
        friendApi.add(loginUserId, friendId);
        //环信添加好友
        huanXinTemplate.makeFriends(loginUserId, friendId);
    }

    /**
     * 分页查询好友
     * @param page
     * @param pagesize
     * @param keyword
     * @return
     */
    public PageResult<ContactVo> queryContactsList(Integer page, Integer pagesize, String keyword) {
        //调用api查询好友
        PageResult result = friendApi.findPage(UserHolder.getUserId(), page, pagesize, keyword);
        //转换
        List<Friend> items = result.getItems();
        List<ContactVo> list = new ArrayList<>();
        for (Friend friend : items) {
            ContactVo vo = new ContactVo();
            UserInfo userInfo = userInfoApi.findById(friend.getFriendId());
            BeanUtils.copyProperties(userInfo, vo);
            vo.setCity(StringUtils.substringBefore(userInfo.getCity(), "-"));
            vo.setUserId(userInfo.getId().toString());
            list.add(vo);
        }
        //添加vo集合
        result.setItems(list);
        return result;
    }

    /**
     * 分页查询点赞、评论、喜欢
     * @param commentType
     * @param page
     * @param pagesize
     * @return
     */
    public ResponseEntity messageCommentList(Integer commentType, Integer page, Integer pagesize) {
        //调用API
        PageResult result = commentApi.findByUserId(page, pagesize, commentType, UserHolder.getUserId());
        List<Comment> items = result.getItems();
        List<MessageVo> list = new ArrayList<>();
        //转换
        for (Comment comment : items) {
            MessageVo vo = new MessageVo();
            vo.setId(comment.getId().toHexString());
            UserInfo userInfo = userInfoApi.findById(comment.getUserId());
            vo.setAvatar(userInfo.getAvatar());
            vo.setNickname(userInfo.getNickname());
            String createDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(comment.getCreated()));
            vo.setCreateDate(createDate);
            list.add(vo);
        }
        result.setItems(list);
        return ResponseEntity.ok(result);
    }
}

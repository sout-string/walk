package com.tanhua.server.service;


import com.tanhua.commons.templates.TencentSmsTemplate;
import com.tanhua.domain.db.*;
import com.tanhua.domain.exception.TanHuaException;
import com.tanhua.domain.vo.ErrorResult;
import com.tanhua.domain.vo.PageResult;
import com.tanhua.domain.vo.SettingVo;
import com.tanhua.domain.vo.UserInfoVoAge;
import com.tanhua.dubbo.api.*;
import com.tanhua.server.interceptor.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;



/**
 * @author : TuGen
 * @date : 2021/5/8 23:23
 */
@Service
@Slf4j
public class SettingService {
    @Reference
    private SettingsApi settingsApi;
    @Reference
    private QuestionApi questionApi;
    @Reference
    private BlackListApi blackListApi;
    @Reference
    private UserInfoApi userInfoApi;
    @Reference
    private UserApi userApi;
    @Autowired
    private TencentSmsTemplate tencentSmsTemplate;
    @Value("${tanhua.redisValidateCodeKeyPrefix}")
    private String redisValidateCodeKeyPrefix;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 查询用户通用设置
     * @return
     */
    public ResponseEntity querySettings() {
        //获取当前用户
        User user = UserHolder.getUser();
        SettingVo vo = new SettingVo();
        //查询陌生人问题
        Question question = questionApi.findByUserId(user.getId());
        if (question != null) {
            vo.setStrangerQuestion(question.getTxt());
        }
        //查询用户的通知设置
        Settings settings = settingsApi.findByUserId(user.getId());
        if (settings != null) {
            BeanUtils.copyProperties(settings, vo);
        }
        //构造返回体
        vo.setPhone(user.getMobile());
        return ResponseEntity.ok(vo);
    }

    /**
     * 添加或更新通知信息
     * @param like
     * @param pinglun
     * @param gonggao
     * @return
     */
    public ResponseEntity updateNotification(Boolean like, Boolean pinglun, Boolean gonggao) {
        Settings settings = settingsApi.findByUserId(UserHolder.getUserId());
        if (settings == null) {
            settings = new Settings();
            settings.setUserId(UserHolder.getUserId());
            settings.setLikeNotification(like);
            settings.setPinglunNotification(pinglun);
            settings.setGonggaoNotification(gonggao);
            settingsApi.save(settings);
        } else {
            settings.setLikeNotification(like);
            settings.setPinglunNotification(pinglun);
            settings.setGonggaoNotification(gonggao);
            settingsApi.update(settings);
        }
        return ResponseEntity.ok(null);
    }

    /**
     * 查询黑名单
     * @param page
     * @param pagesize
     * @return
     */
    public PageResult<UserInfoVoAge> findPageBlackList(Long page, Long pagesize) {
        //获取用户ID
        Long userId = UserHolder.getUserId();
        //分页查询
        PageResult pageResult = blackListApi.findPageBlackList(page, pagesize, userId);
        //根据黑名单用户ID批量查询用户信息表
        List<BlackList> items = pageResult.getItems();
        //取出所有的用户ID
        List<Long> blackUserIds=new ArrayList<>();
        for (BlackList item : items) {
            Long blackUserId = item.getBlackUserId();
            blackUserIds.add(blackUserId);
        }
        //批量查询
        List<UserInfo> userInfoList = userInfoApi.selectByIds(blackUserIds);
        //将userInfo转为userInfoVo
        //分页结果集
        if (CollectionUtils.isNotEmpty(userInfoList)) {
            //接收集合，转为目标集合
            ArrayList<UserInfoVoAge> list = new ArrayList<>();
            //遍历
            for (UserInfo userInfo : userInfoList) {
                //创建vo
                UserInfoVoAge vo = new UserInfoVoAge();
                //转换
                BeanUtils.copyProperties(userInfo, vo);
                //添加
                list.add(vo);
            }
            //更新结果
            pageResult.setItems(list);
        }
        return pageResult;
    }

    /**
     * 移除黑名单用户
     * @param deleteUserId
     * @return
     */
    public ResponseEntity deleteBlackList(long deleteUserId) {
        Long userId = UserHolder.getUserId();
        blackListApi.deleteBlackList(userId,deleteUserId);
        return ResponseEntity.ok(null);
    }

    /**
     * 保存或更新陌生人问题
     * @param content
     * @return
     */
    public ResponseEntity saveQuestions(String content) {
        //查询当前陌生人问题
        Question oldQuestions = questionApi.findByUserId(UserHolder.getUserId());
        //判断保存还是更新
        if (oldQuestions == null) {
            //保存
            oldQuestions = new Question();
            oldQuestions.setTxt(content);
            oldQuestions.setUserId(UserHolder.getUserId());
            questionApi.saveQuestions(oldQuestions);
        } else {
            //更新
            oldQuestions.setTxt(content);
            questionApi.updateQuestions(oldQuestions);
        }
        return ResponseEntity.ok(null);
    }

    /**
     * 修改手机号，发送验证码
     */
    public void sendVerificationCode() {
        //查询用户
        User user = userApi.findById(UserHolder.getUserId());
        //redis中存入验证码的key
        String key = redisValidateCodeKeyPrefix + user.getMobile();
        //获取redis中验证码
        String codeInRedis = (String) redisTemplate.opsForValue().get(key);
        //判断是否存在验证码
        if (!StringUtils.isEmpty(codeInRedis)) {
            throw new TanHuaException(ErrorResult.duplicate());
        }
        //生成验证码
        String validateCode = RandomStringUtils.randomNumeric(6);
        //发送验证码
        log.debug("发送验证码：{}；{}", user.getMobile(), validateCode);
        Map<String, String> smsRs = tencentSmsTemplate.sendValidateCode(user.getMobile(), validateCode);
        if (smsRs!=null) {
            throw new TanHuaException(ErrorResult.fail());
        }
        //验证码存入redis
        log.info("验证码存入redis");
        redisTemplate.opsForValue().set(key, validateCode, 10, TimeUnit.MINUTES);
    }

    /**
     * 校验验证码
     * @param param
     * @return
     */
    public boolean checkVerificationCode( Map<String, String> param) {
        //获取用户
        User user = userApi.findById(UserHolder.getUserId());
        //制造redis key
        String key = redisValidateCodeKeyPrefix + user.getMobile();
        //获取redis中验证码
        String codeInRedis = (String) redisTemplate.opsForValue().get(key);
        //判断是否存在验证码
        if (StringUtils.isEmpty(codeInRedis)) {
            throw new TanHuaException(ErrorResult.error("0003","验证码已过期"));
        }
        if (!StringUtils.equals(codeInRedis, param.get("verificationCode"))) {
            return false;
        }
        //删除redis中验证码
        redisTemplate.delete(key);
        return true;
    }

    /**
     * 修改手机号
     * @param param
     * @param token
     */
    public void changeMobile(Map<String, String> param, String token) {
        //查询用户信息
        Long userId = UserHolder.getUserId();
        User user = userApi.findById(userId);
        //更改手机号码
        user.setMobile(param.get("phone"));
        //更新用户信息
        userApi.updateById(user);
        //删除原有token,让用户重新登录
        Boolean delete = redisTemplate.delete("TOKEN_" + token);
    }
}

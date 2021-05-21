package com.tanhua.server.service;

import com.alibaba.fastjson.JSON;
import com.tanhua.commons.templates.*;
import com.tanhua.domain.db.User;
import com.tanhua.domain.db.UserInfo;
import com.tanhua.domain.exception.TanHuaException;
import com.tanhua.domain.mongo.RecommendUser;
import com.tanhua.domain.vo.*;
import com.tanhua.dubbo.api.UserApi;
import com.tanhua.dubbo.api.UserInfoApi;
import com.tanhua.dubbo.api.UserLikeApi;
import com.tanhua.server.interceptor.UserHolder;
import com.tanhua.server.utils.GetAgeUtil;
import com.tanhua.server.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * @author : 涂根
 * @date : 2021/05/03 下午 8:09
 */
@Service
@Slf4j
public class UserService {
    @Reference
    private UserApi userApi;
    @Reference
    private UserInfoApi userInfoApi;
    @Autowired
    private SmsTemplate smsTemplate;
    @Autowired
    private FaceTemplate faceTemplate;
    @Autowired
    private OssTemplate ossTemplate;
    @Autowired
    private TencentSmsTemplate tencentSmsTemplate;
    @Autowired
    private HuanXinTemplate huanXinTemplate;
    @Value("${tanhua.redisValidateCodeKeyPrefix}")
    private String redisValidateCodeKeyPrefix;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private JwtUtils jwtUtils;
    @Reference
    private UserLikeApi userLikeApi;

    public User findUser(String phone) {
        return userApi.findByMobile(phone);
    }

    public void saveUser(User user) {
        userApi.save(user);
    }

    /**
     * 发送验证码并时效性存入redis
     *
     * @param phone 用户电话号
     */
    public void sendValidateCode(String phone) {
        //redis中存入验证码的key
        String key = redisValidateCodeKeyPrefix + phone;
        //获取redis中验证码
        String codeInRedis = (String) redisTemplate.opsForValue().get(key);
        //判断是否存在验证码
        if (!StringUtils.isEmpty(codeInRedis)) {
            throw new TanHuaException(ErrorResult.duplicate());
        }
        //生成验证码
        String validateCode = RandomStringUtils.randomNumeric(6);
        //发送验证码
        log.debug("发送验证码：{}；{}", phone, validateCode);
        Map<String, String> smsRs = tencentSmsTemplate.sendValidateCode(phone, validateCode);
        if (smsRs != null) {
            throw new TanHuaException(ErrorResult.fail());
        }
        //验证码存入redis
        log.info("验证码存入redis");
        redisTemplate.opsForValue().set(key, validateCode, 10, TimeUnit.MINUTES);

    }

    /**
     * 登录并验证、自动注册
     *
     * @param param 用户信息
     * @return 返回新用户与否、token
     */
    public Map<String, Object> loginVerification(Map<String, String> param) {
        //获取用户手机号、验证码
        String phone = param.get("phone");
        String verificationCode = param.get("verificationCode");
        //存入验证码的key
        String key = redisValidateCodeKeyPrefix + phone;
        //获取redis中验证码
        String codeInRedis = (String) redisTemplate.opsForValue().get(key);
        //日志记录
        log.info("校验 验证码：{}；{}；{}", phone, codeInRedis, verificationCode);
        //判断是否取到验证码
        if (StringUtils.isEmpty(codeInRedis)) {
            //验证码为空，告诉用户验证码过期
            throw new TanHuaException(ErrorResult.loginError());
        }
        //读取到了redis中验证码
        //判断验证码是否相同
        if (!StringUtils.equals(codeInRedis, verificationCode)) {
            //验证码不同，告诉用户验证码错误
            throw new TanHuaException(ErrorResult.validateCodeError());
        }
        //删除redis中验证码，防止多次验证
        redisTemplate.delete(key);
        //查找用户
        User user = userApi.findByMobile(phone);
        boolean isNew = false;
        //判断是否存在该用户
        if (user == null) {
            //不存在用户，自动注册
            user = new User();
            user.setMobile(phone);
            //手机号后六位作为密码并加密
            user.setPassword(DigestUtils.md5Hex(phone.substring(5, 11)));
            //日志记录新用户
            log.info("添加新用户：{}", phone);
            //存入数据库
            Long userId = userApi.save(user);
            user.setId(userId);
            isNew = true;
            //注册环信通讯 先在判断外跑一次
            log.info("为用户" + user.getId() + "注册环信账户");
            huanXinTemplate.register(user.getId());
        }
        //签发token
        String token = jwtUtils.createJWT(phone, user.getId());
        //用户信息存入redis，方便后续使用，时效一天
        String userString = JSON.toJSONString(user);
        log.info("token存入redis：" + "TOKEN_" + token);
        redisTemplate.opsForValue().set("TOKEN_" + token, userString, 1, TimeUnit.DAYS);
        //返回结果
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("isNew", isNew);
        resultMap.put("token", token);
        return resultMap;
    }

    /**
     * 根据token获取用户
     *
     * @param token 用户的token
     * @return 返回用户
     */
    public User getUserByToken(String token) {
        String key = "TOKEN_" + token;
        String userJsonStr = (String) redisTemplate.opsForValue().get(key);
        if (StringUtils.isEmpty(userJsonStr)) {
            return null;
        }
        //续期
        redisTemplate.expire(key, 1, TimeUnit.DAYS);
        User user = JSON.parseObject(userJsonStr, User.class);
        return user;
    }

    /**
     * 保存用户信息
     *
     * @param userInfo
     * @param token
     */
    public void saveUserInfo(UserInfo userInfo, String token) {
        User user = UserHolder.getUser();
        userInfo.setId(user.getId());
        userInfoApi.add(userInfo);
    }

    /**
     * 上传用户头像
     * 更新用户头像
     *
     * @param headPhoto
     * @param token
     */
    public void updateAvatar(MultipartFile headPhoto, String token) {
        //获取用户信息，判断是更新还是上传
        User oldUser = UserHolder.getUser();
        UserInfo oldUserInfo = userInfoApi.findById(oldUser.getId());
        Boolean haveAvatar = false;
        if (StringUtils.isNotEmpty(oldUserInfo.getAvatar())) {
            haveAvatar = true;
        }
        try {
            String filename = headPhoto.getOriginalFilename();
            byte[] bytes = headPhoto.getBytes();
            //人脸检测
            if (!faceTemplate.detect(bytes)) {
                throw new TanHuaException();
            }
            //上传头像
            String avatar = ossTemplate.upload(filename, headPhoto.getInputStream());
            //删除原有头像
            if (haveAvatar) {
                ossTemplate.delete(oldUserInfo.getAvatar());
            }
            //更新用户头像
            UserInfo userInfo = new UserInfo();
            userInfo.setId(oldUser.getId());
            userInfo.setAvatar(avatar);
            userInfoApi.update(userInfo);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("上传头像失败", e);
            throw new TanHuaException("上传头像失败，请稍后重试");
        }
    }

    public UserInfoVo findUserInfoById(Long id) {
        UserInfo userInfo = userInfoApi.findById(id);
        UserInfoVo userInfoVo = new UserInfoVo();
        BeanUtils.copyProperties(userInfo, userInfoVo);
        return userInfoVo;
    }

    /**
     * 更新用户信息
     *
     * @param vo
     * @param token
     */
    public void updateUserInfo(UserInfoVo vo, String token) {
        User user = UserHolder.getUser();
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(vo, userInfo);
        //通过生日计算年龄
        int age = GetAgeUtil.getAge(userInfo.getBirthday());
        userInfo.setAge(age);
        userInfo.setId(user.getId());
        userInfoApi.update(userInfo);
    }

    /**
     * 统计粉丝信息
     *
     * @return
     */
    public CountsVo counts() {
        Long loginUserId = UserHolder.getUserId();
        //统计喜欢
        Long likeCount = userLikeApi.countLike(loginUserId);
        //统计喜欢我（粉丝）
        Long fansCount = userLikeApi.countFans(loginUserId);
        //统计相互喜欢（好友个数）
        Long loveEachOtherCount = userLikeApi.countLikeEachOther(loginUserId);
        CountsVo countsVo = new CountsVo();
        countsVo.setFanCount(fansCount);
        countsVo.setEachLoveCount(loveEachOtherCount);
        countsVo.setLoveCount(likeCount);
        return countsVo;
    }

    /**
     * 好友、喜欢、粉丝、访客列表分页查询
     *
     * @param page
     * @param pagesize
     * @param type
     * @return
     */
    public PageResult<FriendVo> queryUserLikeList(int page, int pagesize, int type) {
        Long loginUserId = UserHolder.getUserId();
        //准备结果容器与标记列表是否为已登录用户喜欢
        PageResult result = new PageResult();
        Boolean alreadyLove = false;
        //根据类型查询
        switch (type) {
            case 1:
                //查询好友
                result = userLikeApi.findPageLikeEachOther(loginUserId, page, pagesize);
                alreadyLove = true;
                break;
            case 2:
                //查询我喜欢
                result = userLikeApi.findPageOneSideLike(loginUserId, page, pagesize);
                alreadyLove = true;
                break;
            case 3:
                //查询喜欢我
                result = userLikeApi.findPageFans(loginUserId, page, pagesize);
                break;
            case 4:
                //查询访客列表
                result = userLikeApi.findPageMyVisitors(loginUserId, page, pagesize);
                break;
            default:
                break;
        }
        //转换
        List<FriendVo> list = new ArrayList<>();
        List<RecommendUser> items = result.getItems();
        //非空判断
        if (!CollectionUtils.isEmpty(items)) {
            for (RecommendUser recommendUser : items) {
                FriendVo vo = new FriendVo();
                //查询对方信息
                Long userId = recommendUser.getUserId();
                UserInfo userInfo = userInfoApi.findById(userId);
                BeanUtils.copyProperties(userInfo, vo);
                vo.setAlreadyLove(alreadyLove);
                vo.setMatchRate(recommendUser.getScore().intValue());
                list.add(vo);
            }
            //赋予vo
            result.setItems(list);
        }
        //返回结果
        return result;
    }

    /**
     * 关注用户
     * @param fansId
     */
    public void fansLike(Long fansId) {
        Long loginUserId = UserHolder.getUserId();
        Boolean flag = userInfoApi.like(loginUserId, fansId);
        if (flag) {
            huanXinTemplate.makeFriends(loginUserId,fansId);
        }

    }
}

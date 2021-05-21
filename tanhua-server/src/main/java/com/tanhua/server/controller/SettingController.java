package com.tanhua.server.controller;

import com.tanhua.domain.vo.PageResult;
import com.tanhua.domain.vo.UserInfoVoAge;
import com.tanhua.server.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : TuGen
 * @date : 2021/5/8 23:17
 */
@RestController
@RequestMapping("/users")
public class SettingController {
    @Autowired
    private SettingService settingService;

    /**
     * 获取用户设置
     * @return
     */
    @GetMapping("/settings")
    public ResponseEntity querySettings() {
        return settingService.querySettings();
    }

    /**
     * 更改用户设置
     * @param map
     * @return
     */
    @PostMapping("/notifications/setting")
    public ResponseEntity updateNotification(@RequestBody Map map) {
        //获取输入内容
        Boolean like = (Boolean) map.get("likeNotification");
        Boolean pinglun = (Boolean) map.get("pinglunNotification");
        Boolean gonggao = (Boolean) map.get("gonggaoNotification");
        return settingService.updateNotification(like, pinglun, gonggao);

    }

    /**
     * 分页查询黑名单
     *
     * @param page
     * @param pagesize
     * @return
     */
    @GetMapping("/blacklist")
    public ResponseEntity findBlackList(@RequestParam(defaultValue = "1") Long page, @RequestParam(defaultValue = "10") Long pagesize) {
        PageResult<UserInfoVoAge> pageResult = settingService.findPageBlackList(page, pagesize);
        return ResponseEntity.ok(pageResult);
    }

    /**
     * 删除黑名单用户
     *
     * @param deleteUserId
     * @return
     */
    @DeleteMapping("/blacklist/{uid}")
    public ResponseEntity deleteBlackList(@PathVariable("uid") long deleteUserId) {
        return settingService.deleteBlackList(deleteUserId);
    }

    /**
     * 保存陌生人问题
     *
     * @param map
     * @return
     */
    @PostMapping("/questions")
    public ResponseEntity saveQuestions(@RequestBody Map map) {
        //获取问题内容
        String content = (String) map.get("content");
        //调用
        return settingService.saveQuestions(content);
    }

    /**
     * 修改手机号发送验证码
     *
     * @return
     */
    @PostMapping("/phone/sendVerificationCode")
    public ResponseEntity sendVerificationCode() {
        settingService.sendVerificationCode();
        return ResponseEntity.ok(null);
    }

    /**
     * 校验验证码
     *
     * @param param
     * @return
     */
    @PostMapping("/phone/checkVerificationCode")
    public ResponseEntity checkVerificationCode(@RequestBody Map<String, String> param) {
        boolean flag = settingService.checkVerificationCode(param);
        HashMap<String, Boolean> result = new HashMap<>();
        result.put("verification", flag);
        return ResponseEntity.ok(result);
    }

    /**
     * 保存修改后的手机号码
     * @param param
     * @param token
     * @return
     */
    @PutMapping("/phone")
    public ResponseEntity changeMobile(@RequestBody Map<String, String> param, @RequestHeader("Authorization") String token) {
        settingService.changeMobile(param, token);
        return ResponseEntity.ok(null);
    }
}

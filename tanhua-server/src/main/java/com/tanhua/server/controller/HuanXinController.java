package com.tanhua.server.controller;

import com.tanhua.commons.vo.HuanXinUser;
import com.tanhua.server.interceptor.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : TuGen
 * @date : 2021/5/16 17:49
 */
@RestController
@RequestMapping("/huanxin")
@Slf4j
public class HuanXinController {
    /**
     * 获取当前登录用户的用户名与密码，用于环信登录
     * @return
     */
    @GetMapping("/user")
    public ResponseEntity<HuanXinUser> getLoginHuanXinUser() {
        HuanXinUser user = new HuanXinUser(UserHolder.getUserId().toString(), "123456", "aaa");
        log.info("应用请求了用户"+UserHolder.getUserId()+"的环信账户信息");
        return ResponseEntity.ok(user);
    }
}

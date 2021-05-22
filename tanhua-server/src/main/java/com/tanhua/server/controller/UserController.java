package com.tanhua.server.controller;

import com.tanhua.domain.db.UserInfo;
import com.tanhua.domain.vo.UserInfoVo;
import com.tanhua.server.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author : TuGen
 * @date : 2021/5/7 10:38
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 保存用户信息
     * @param vo
     * @param token
     * @return
     */
    @PostMapping("/loginReginfo")
    public ResponseEntity loginReginfo(@RequestBody UserInfoVo vo, @RequestHeader("Authorization") String token) {
        log.info("UserController-保存用户信息");
        UserInfo userInfo = new UserInfo();
        //复制属性
        BeanUtils.copyProperties(vo, userInfo);
        //保存用户信息
        userService.saveUserInfo(userInfo, token);
        return ResponseEntity.ok(null);
    }

    /**
     * 上传用户头像
     * @param headPhoto
     * @param token
     * @return
     */
    @PostMapping("/loginReginfo/head")
    public ResponseEntity uploadAvatar(MultipartFile headPhoto, @RequestHeader("Authorization") String token) throws IOException {
        log.info("UserController-上传用户头像");
        userService.updateAvatar(headPhoto, token);
        return ResponseEntity.ok(null);
    }
}

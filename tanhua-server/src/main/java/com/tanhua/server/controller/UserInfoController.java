package com.tanhua.server.controller;

import com.tanhua.domain.db.User;
import com.tanhua.domain.exception.TanHuaException;
import com.tanhua.domain.vo.CountsVo;
import com.tanhua.domain.vo.FriendVo;
import com.tanhua.domain.vo.PageResult;
import com.tanhua.domain.vo.UserInfoVo;
import com.tanhua.server.interceptor.UserHolder;
import com.tanhua.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author : TuGen
 * @date : 2021/5/7 10:38
 */
@RestController
@RequestMapping("/users")
public class UserInfoController {
    @Autowired
    private UserService userService;

    /**
     * 通过ID获取用户信息
     *
     * @param userID
     * @param huanxinID
     * @param token
     * @return
     */
    @GetMapping
    public ResponseEntity getUserById(Long userID, Long huanxinID, @RequestHeader("Authorization") String token) {
        //
        Long userId = UserHolder.getUserId();
        //查询用户id
        UserInfoVo userInfoVo = userService.findUserInfoById(userId);
        return ResponseEntity.ok(userInfoVo);
    }

    /**
     * 更新用户信息
     *
     * @param vo
     * @param token
     * @return
     */
    @PutMapping
    public ResponseEntity updateUserInfo(@RequestBody UserInfoVo vo, @RequestHeader("Authorization") String token) {
        userService.updateUserInfo(vo, token);
        return ResponseEntity.ok(null);
    }

    /**
     * 上传头像
     *
     * @param headPhoto
     * @param token
     * @return
     */
    @PostMapping
    public ResponseEntity header(MultipartFile headPhoto, @RequestHeader("Authorization") String token) {
        userService.updateAvatar(headPhoto, token);
        return ResponseEntity.ok(null);
    }

    /**
     * 统计粉丝信息
     * @return
     */
    @GetMapping("/counts")
    public ResponseEntity counts() {
        CountsVo countsVo = userService.counts();
        return ResponseEntity.ok(countsVo);
    }

    /**
     * 好友、喜欢、粉丝分页查询
     *
     * @return
     */
    @GetMapping("/friends/{type}")
    public ResponseEntity queryUserLikeList(@PathVariable int type, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pagesize) {
        page = page > 0 ? page : 1;
        PageResult<FriendVo> result = userService.queryUserLikeList(page, pagesize, type);
        return ResponseEntity.ok(result);
    }

    /**
     * 喜欢（关注）用户
     * @param uid
     * @return
     */
    @PostMapping("/fans/{uid}")
    public ResponseEntity fansLike(@PathVariable("uid") Long uid) {
        userService.fansLike(uid);
        return ResponseEntity.ok(null);
    }
}

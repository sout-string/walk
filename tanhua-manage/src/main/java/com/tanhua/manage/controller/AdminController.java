package com.tanhua.manage.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.tanhua.manage.domain.Admin;
import com.tanhua.manage.interceptor.AdminHolder;
import com.tanhua.manage.service.AdminService;
import com.tanhua.manage.vo.AdminVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/system/users")
@Slf4j
public class AdminController {

    @Autowired
    private AdminService adminService;

    /**
     * 后台登陆时 图片验证码 生成
     */
    @GetMapping("/verification")
    public void showValidateCodePic(String uuid,HttpServletRequest req, HttpServletResponse res){
        res.setDateHeader("Expires",0);
        res.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        res.addHeader("Cache-Control", "post-check=0, pre-check=0");
        // Set standard HTTP/1.0 no-cache header.
        res.setHeader("Pragma", "no-cache");
        res.setContentType("image/jpeg");
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(299, 97);
        String code = lineCaptcha.getCode();
        log.debug("uuid={},code={}",uuid,code);
        adminService.saveCode(uuid,code);
        try {
            lineCaptcha.write(res.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 用户登录
     *  POST  /login
     *  参数：
     *
     */
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody Map<String,String> map) {
        return adminService.login(map);
    }
    /**
     * 获取当前登录用户的详情：
     *  POST    /profile
     *  在请求头中包含一个 Authorization 数据(token元素)
     *     Bearer token
     *  返回AdminVo
     */
    @PostMapping("/profile")
    public ResponseEntity profile() {
        Admin admin = AdminHolder.getAdmin();
        AdminVo vo = new AdminVo();
        BeanUtils.copyProperties(admin, vo);
        return ResponseEntity.ok(vo);
    }
    /**
     * 退出登录
     *  POST  /logout
     */
    @PostMapping("/logout")
    public ResponseEntity logout(@RequestHeader("Authorization") String token) {
        token = token.replace("Bearer ", "");
        return adminService.logout(token);
    }
}
package com.tanhua.server.controller;

import com.tanhua.domain.db.User;
import com.tanhua.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author: 涂根
 * @date: 2021/05/03 下午 7:56
 */
@RestController
@RequestMapping("/user")
public class LoginController {
    @Autowired(required = false)
    private UserService userService;

    @RequestMapping("/findUser")
    public ResponseEntity findUser(String phone) {
        User user = userService.findUser(phone);
        return ResponseEntity.ok(user);
    }
    @RequestMapping("/saveUser")
    public ResponseEntity saveUser(@RequestBody User user) {
        userService.saveUser(user);
        return ResponseEntity.ok(null);
    }
    @RequestMapping("/login")
    public ResponseEntity sendValidateCode(@RequestBody Map<String, String> param) {
        String phone = param.get("phone");
        userService.sendValidateCode(phone);
        return ResponseEntity.ok(null);
    }
    @RequestMapping("/loginVerification")
    public ResponseEntity loginVerification(@RequestBody Map<String, String> param) {
        Map<String, Object> resultMap = userService.loginVerification(param);
        return ResponseEntity.ok(resultMap);
    }
}

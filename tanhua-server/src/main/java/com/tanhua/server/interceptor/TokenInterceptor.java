package com.tanhua.server.interceptor;

import com.tanhua.domain.db.User;
import com.tanhua.server.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author TuGen
 * @date 2021/5/8 20:20
 */
@Component
@Slf4j
public class TokenInterceptor implements HandlerInterceptor {
    @Autowired
    private UserService userService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("进入前置拦截器："+request.getRequestURI());
        //获取请求中token
        String token = request.getHeader("Authorization");
        //验证token
        if (!StringUtils.isEmpty(token)) {
            //获取User对象
            User userByToken = userService.getUserByToken(token);
            //判断有无该User
            if (userByToken == null) {
                //无对象，拦截
                log.info("该请求token无效，未放行");
                response.setStatus(401);
                return false;
            }
            //有对象，存储并放行
            UserHolder.setUser(userByToken);
            return true;
        }
        //没有token，拦截
        log.info("该请求无token，未放行");
        response.setStatus(401);
        return false;
    }
}


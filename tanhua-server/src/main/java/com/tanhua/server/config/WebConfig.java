package com.tanhua.server.config;

import com.tanhua.server.interceptor.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author : TuGen
 * @date : 2021/5/9 0:10
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private TokenInterceptor tokenInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //设置全部拦截与放行的部分
        registry.addInterceptor(tokenInterceptor).addPathPatterns("/**").excludePathPatterns("/user/login", "/user/loginVerification");
    }
}

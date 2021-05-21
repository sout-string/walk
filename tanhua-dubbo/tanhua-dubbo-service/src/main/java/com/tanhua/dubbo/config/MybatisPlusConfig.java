package com.tanhua.dubbo.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : TuGen
 * @date : 2021/5/8 23:10
 */
@Configuration
public class MybatisPlusConfig {
    /**
     * 开启分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}

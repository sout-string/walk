package com.tanhua.dubbo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author: 涂根
 * @date: 2021/05/03 下午 7:49
 */
@SpringBootApplication
@MapperScan("com.tanhua.dubbo.mapper")
public class TanhuaDubboApplication {
    public static void main(String[] args) {
        SpringApplication.run(TanhuaDubboApplication.class, args);
    }
}

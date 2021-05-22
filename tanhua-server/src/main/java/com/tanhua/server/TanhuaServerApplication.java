package com.tanhua.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;

/**
 * @author: 涂根
 * @date: 2021/05/03 下午 8:12
 */
@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
public class TanhuaServerApplication {
    /**
     * redisTemplate默认的key序列化是jdk
     * 修改它为字符串
     * @param redisTemplate
     */
    @Resource
    public void setKeySerializer(RedisTemplate redisTemplate) {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
    }

    public static void main(String[] args) {
        SpringApplication.run(TanhuaServerApplication.class, args);
    }
}

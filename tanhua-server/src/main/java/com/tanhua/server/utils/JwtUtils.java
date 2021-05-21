package com.tanhua.server.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;

/**
 * @author: 涂根
 * @date: 2021/05/04 下午 4:10
 */
@Component
public class JwtUtils {

    @Value("${tanhua.secret}")
    private  String secret;

    public String createJWT(String phone, Long userId) {
        //创建集合，用于制作头;Claims类是个接口，实质上还是个集合，直接自己建，避免实现方法，麻烦
        HashMap<String, Object> claims = new HashMap<>(2);
        claims.put("mobile", phone);
        claims.put("id", userId);
        //制作时间戳
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        //加盐加密
        JwtBuilder builder = Jwts.builder().setClaims(claims).setIssuedAt(now).signWith(SignatureAlgorithm.HS256, secret);
        return builder.compact();
    }
}

package com.tanhua.server.controller;

import com.tanhua.domain.vo.SoulListVo;
import com.tanhua.server.service.SoulService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @description
 * @author:Dell
 * @date:2021/5/23 11:00
 */

@RestController
@RequestMapping("/testSoul")
public class SoulController {
    @Autowired
    SoulService soulService;
    /**
     * 测灵魂-问卷列表（学生实战）
     */
    @GetMapping
    public ResponseEntity testSoul() {
        List<SoulListVo> result=soulService.testSoul();
        return  ResponseEntity.ok(result);
    }
}

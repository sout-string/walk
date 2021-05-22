package com.tanhua.server.controller;

import com.tanhua.commons.templates.OssTemplate;
import com.tanhua.domain.exception.TanHuaException;
import com.tanhua.server.interceptor.UserHolder;
import com.tanhua.server.service.VoiceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author atsing
 * @date 2021/5/22
 */
@RestController
@RequestMapping("/peachblossom")
@Slf4j
public class VoiceController {

    @Autowired
    private OssTemplate ossTemplate;

    @Autowired
    private VoiceService voiceService;

    //    推送传递的语音文件;
    @PostMapping
    public void sendVoice(MultipartFile soundFile) {
        Long userId = UserHolder.getUserId();
        String originalFilename = soundFile.getOriginalFilename();
        String filePath = null;
        try {
            filePath = ossTemplate.upload(originalFilename, soundFile.getInputStream());
        } catch (IOException e) {
//            e.printStackTrace();
            log.error("上传文件失败, 原因系:{}", e);
        }
        if (StringUtils.isEmpty(filePath)) {
            new TanHuaException("上传文件失败，未获取到文件路径信息；");
        }
//        将语音文件存入mongodb 中，进行存储； 存储内容为 当前用户的当前用户的 id  ， 语音文件的地址；
        voiceService.sendVoice(userId, filePath);
    }
}

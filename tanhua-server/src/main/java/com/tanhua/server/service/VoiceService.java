package com.tanhua.server.service;

import com.tanhua.dubbo.api.mongo.VoiceApi;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;

/**
 * @author atsing
 * @date 2021/5/22
 */
@Service
public class VoiceService {
    @Reference
    private VoiceApi voiceApi;

    public void sendVoice(Long userId, String filePath) {
        voiceApi.sendVoice(userId,filePath);
    }
}

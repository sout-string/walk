package com.tanhua.dubbo.api.mongo;

/**
 * @author atsing
 * @date 2021/5/22
 */
public interface VoiceApi {
    void sendVoice(Long userId, String filePath);
}

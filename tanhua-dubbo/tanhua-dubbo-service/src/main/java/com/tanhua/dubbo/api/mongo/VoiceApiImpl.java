package com.tanhua.dubbo.api.mongo;

import com.sun.corba.se.spi.ior.ObjectId;
import com.tanhua.domain.mongo.Voice;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * @author atsing
 * @date 2021/5/22
 */
@Service
public class VoiceApiImpl implements VoiceApi{
    @Autowired
    private MongoTemplate mongoTemplate ;

    @Override
    public void sendVoice(Long userId, String filePath) {
        Voice voice = new Voice(userId.intValue(), filePath);
        mongoTemplate.save(voice);
    }

}

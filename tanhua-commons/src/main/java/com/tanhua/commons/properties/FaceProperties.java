package com.tanhua.commons.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author : TuGen
 * @date : 2021/5/6 16:32
 */
@Data
@ConfigurationProperties(prefix = "tanhua.face")
public class FaceProperties {
    private String appId;
    private String apiKey;
    private String secretKey;
}
package com.tanhua.commons.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author: 涂根
 * @date: 2021/05/03 下午 8:15
 */
@Data
@ConfigurationProperties(prefix = "tanhua.sms")
public class SmsProperties {
    /**
     * 签名
     */
    private String signName;
    /**
     * 模板参数名
     */
    private String parameterName;
    /**
     * 验证码短信模板
     */
    private String validateCodeTemplateCode;

    private String accessKeyId;

    private String accessKeySecret;


}

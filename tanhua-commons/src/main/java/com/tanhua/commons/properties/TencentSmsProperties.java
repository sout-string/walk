package com.tanhua.commons.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author : TuGen
 * @date : 2021/5/6 17:49
 */
@Data
@ConfigurationProperties(prefix = "tanhua.tencentsms")
public class TencentSmsProperties {
    /**
     * 短信应用ID
     */
    private String smsSdkAppId;
    /**
     * 验证码模板ID
     */
    private String validateCodeTemplateId;
    /**
     * 短信模板ID
     */
    private String smsTemplateId;
    /**
     * 短信签名内容
     */
    private String sign;
    /**
     * 账户ID
     */
    private String secretId;
    /**
     * 账户密钥
     */
    private String secretKey;

}

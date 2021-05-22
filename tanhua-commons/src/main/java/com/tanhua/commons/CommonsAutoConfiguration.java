package com.tanhua.commons;

import com.tanhua.commons.properties.*;
import com.tanhua.commons.templates.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author: 涂根
 * @date: 2021/05/03 下午 8:51
 */
@Configuration
@EnableConfigurationProperties({SmsProperties.class, OssProperties.class, FaceProperties.class, TencentSmsProperties.class,HuanXinProperties.class})
public class CommonsAutoConfiguration {

    /**
     * 这是测试推送新版本
     * @param smsProperties
     * @return
     */
    @Bean
    public SmsTemplate smsTemplate(SmsProperties smsProperties) {
        SmsTemplate smsTemplate = new SmsTemplate(smsProperties);
        smsTemplate.init();
        return smsTemplate;
    }

    @Bean
    public OssTemplate ossTemplate(OssProperties ossProperties) {
        OssTemplate ossTemplate = new OssTemplate(ossProperties);
        return ossTemplate;
    }

    @Bean
    public FaceTemplate faceTemplate(FaceProperties faceProperties) {
        return new FaceTemplate(faceProperties);
    }

    @Bean
    public TencentSmsTemplate tencentSmsTemplate(TencentSmsProperties tencentSmsProperties) {
        TencentSmsTemplate tencentSmsTemplate = new TencentSmsTemplate(tencentSmsProperties);
        tencentSmsTemplate.inti();
        return tencentSmsTemplate;
    }

    @Bean
    public HuanXinTemplate huanXinTemplate(HuanXinProperties huanXinProperties) {
        HuanXinTemplate huanXinTemplate = new HuanXinTemplate(huanXinProperties);
        return huanXinTemplate;
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}

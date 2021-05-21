package com.tanhua.commons.templates;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.tanhua.commons.properties.SmsProperties;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: 涂根
 * @date: 2021/05/03 下午 8:21
 */
@Slf4j
public class SmsTemplate {
    public static final String SMSRESPONSE_CODE = "code";
    public static final String SMSRESPONSE_MESSAGE = "Message";
    private SmsProperties smsProperties;
    private IAcsClient acsClient;

    public SmsTemplate(SmsProperties smsProperties) {
        this.smsProperties = smsProperties;
    }

    public void init() {
        //设置超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        //设置ascClient初始化参数
        final String product = "Dysmsapi";// 短信API产品名称（短信产品名固定，无需修改）
        final String domain = "dysmsapi.aliyuncs.com";// 短信API产品域名（接口地址固定，无需修改）
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", smsProperties.getAccessKeyId(), smsProperties.getAccessKeySecret());
        try {
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
            acsClient = new DefaultAcsClient(profile);

        } catch (ClientException e) {
            log.error("初始化阿里云短信失败",e);
            //e.printStackTrace();
        }
    }

    public Map<String, String> sendValidateCode(String phoneNumber, String validateCode) {
        return sendSms(smsProperties.getValidateCodeTemplateCode(), phoneNumber, validateCode);
    }

    public Map<String, String> sendSms(String templateCode, String phoneNumber, String param) {
        //组装请求对象
        SendSmsRequest request = new SendSmsRequest();
        //提交post
        request.setMethod(MethodType.POST);
        //发送号码
        request.setPhoneNumbers(phoneNumber);
        //短信签名
        request.setSignName(smsProperties.getSignName());
        //短信模板
        request.setTemplateCode(templateCode);
        //可选 模板中变量替换为json
        request.setTemplateParam(String.format("{\"%s\":\"%s\"}", smsProperties.getParameterName(), param));
        //将发送结果封装至Map
        Map<String, String> result = new HashMap<String, String>(2);
        try {
            SendSmsResponse sendSmaResponse = acsClient.getAcsResponse(request);
            if (("OK").equals(sendSmaResponse.getCode())) {
                return null;
            }
            result.put(SmsTemplate.SMSRESPONSE_CODE, sendSmaResponse.getCode());
            result.put(SmsTemplate.SMSRESPONSE_MESSAGE, sendSmaResponse.getMessage());
        } catch (ClientException e) {
            e.printStackTrace();
            result.put(SmsTemplate.SMSRESPONSE_CODE, "FAIL");
        }
        return result;
    }
}


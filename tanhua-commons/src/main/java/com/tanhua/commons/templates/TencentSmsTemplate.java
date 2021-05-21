package com.tanhua.commons.templates;

import com.tanhua.commons.properties.TencentSmsProperties;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20190711.SmsClient;
import com.tencentcloudapi.sms.v20190711.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20190711.models.SendSmsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;



import java.util.HashMap;
import java.util.Map;

/**
 * @author : TuGen
 * @date : 2021/5/6 18:03
 */
@Slf4j
public class TencentSmsTemplate {
    public static final String SMSRESPONSE_CODE = "code";
    public static final String SMSRESPONSE_MESSAGE = "Message";
    private final TencentSmsProperties tencentSmsProperties;
    private SendSmsRequest req;
    private SmsClient client;

    public TencentSmsTemplate(TencentSmsProperties tencentSmsProperties) {
        this.tencentSmsProperties = tencentSmsProperties;
    }

    public void inti() {
        Credential cred = new Credential(tencentSmsProperties.getSecretId(), tencentSmsProperties.getSecretKey());
        // 实例化一个 http 选项，可选，无特殊需求时可以跳过
        HttpProfile httpProfile = new HttpProfile();
        /* SDK 默认使用 POST 方法。
         * 如需使用 GET 方法，可以在此处设置，但 GET 方法无法处理较大的请求 */
        httpProfile.setReqMethod("POST");
        /* SDK 有默认的超时时间，非必要请不要进行调整
         * 如有需要请在代码中查阅以获取最新的默认值 */
        httpProfile.setConnTimeout(60);
        /* 非必要步骤:
         * 实例化一个客户端配置对象，可以指定超时时间等配置 */
        ClientProfile clientProfile = new ClientProfile();
        /* SDK 默认用 TC3-HMAC-SHA256 进行签名
         * 非必要请不要修改该字段 */
        clientProfile.setSignMethod("HmacSHA256");
        clientProfile.setHttpProfile(httpProfile);
        /* 实例化 SMS 的 client 对象
         * 第二个参数是地域信息，可以直接填写字符串 ap-guangzhou，或者引用预设的常量 */
        client = new SmsClient(cred, "ap-guangzhou", clientProfile);

        /* 实例化一个请求对象，根据调用的接口和实际情况，可以进一步设置请求参数
         * 您可以直接查询 SDK 源码确定接口有哪些属性可以设置
         * 属性可能是基本类型，也可能引用了另一个数据结构
         * 推荐使用 IDE 进行开发，可以方便地跳转查阅各个接口和数据结构的文档说明 */
        req = new SendSmsRequest();


    }

    public Map<String, String> sendValidateCode(String phoneNumber, String validateCode) {
        return sendSms(tencentSmsProperties.getValidateCodeTemplateId(), phoneNumber, validateCode);
    }

    public Map<String, String> sendSms(@Nullable String templateCode, String phoneNumber, String param) {
        //装配信息
        String appId = tencentSmsProperties.getSmsSdkAppId();
        String sign = tencentSmsProperties.getSign();
        String[] phoneNumbers = {"+86" + phoneNumber};
        String[] templateParams = {param};
        req.setSign(sign);
        if (templateCode!=null && templateCode.length()>0) {
            req.setTemplateID(templateCode);
        } else {
            req.setTemplateID(tencentSmsProperties.getSmsTemplateId());
        }
        req.setSmsSdkAppid(appId);
        req.setPhoneNumberSet(phoneNumbers);
        req.setTemplateParamSet(templateParams);
        //将发送结果封装至Map
        Map<String, String> result = new HashMap<>(2);
        String code = "Fail";
        try {
            //发送短信
            log.info("短信发送：号码 = {}，内容 = {}",phoneNumber,param);
            SendSmsResponse res = client.SendSms(req);
            //获取执行结果码
            code = res.getSendStatusSet()[0].getCode();
            //判断是否发送成功
            if ("Ok".equals(code)) {
                //成功
                return null;
            } else {
                log.warn("腾讯云短信业务错误代码：{}",code);
                result.put(SMSRESPONSE_CODE, code);
                result.put(SMSRESPONSE_MESSAGE, res.getSendStatusSet()[0].getMessage());
            }
        } catch (TencentCloudSDKException e) {
            e.printStackTrace();
            result.put(SMSRESPONSE_CODE, code);
        }
        return result;
    }

}




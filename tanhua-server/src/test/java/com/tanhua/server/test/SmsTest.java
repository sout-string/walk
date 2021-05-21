package com.tanhua.server.test;

import com.tanhua.commons.templates.SmsTemplate;
import com.tanhua.commons.templates.TencentSmsTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

/**
 * @author : 涂根
 * @date : 2021/05/03 下午 8:57
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class SmsTest {
    @Autowired
    private SmsTemplate smsTemplate;
    @Autowired
    private TencentSmsTemplate tencentSmsTemplate;

    @Test
    public void testSms() {
      /*  Map<String, String> stringStringMap = smsTemplate.sendValidateCode("18270910531", "666666");
        System.out.println(stringStringMap);*/
    }
    @Test
    public void testTencentSms() {
     /*   Map<String, String> stringStringMap = tencentSmsTemplate.sendValidateCode("18270910531", "000000");
        System.out.println(stringStringMap);*/
    }
}

package com.tanhua.server.test;

import com.alibaba.fastjson.JSON;
import com.tanhua.commons.templates.HuanXinTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class HuanXinTest {

    @Autowired
    private HuanXinTemplate huanXinTemplate;

    /**
     * 注册
     */
    @Test
    public void testRegister(){
    /*    huanXinTemplate.register(10009l);*/
        // huanXinTemplate.register(1l);
    }

    @Test
    public void registerBatch(){
   /*     huanXinTemplate.registerBatch();*/
    }

    /**
     * 建立好友关系
     */
    @Test
    public void makeFriend(){
    /*    huanXinTemplate.makeFriends(1l,10009l);*/
    }

    /**
     * 发送消息
     */
    @Test
    public void sendMsg() {
      /*  String ab = "{\"userId\": \"1\",\"nickname\":\"黑马小妹\",\"strangerQuestion\": \"你喜欢去看蔚蓝的大海还是去爬巍峨的高山？\",\"reply\": \"我喜欢秋天的落叶，夏天的泉水，冬天的雪地，只要有你一切皆可~\"}";
        Map<String, Object> map = new HashMap<>();
        map.put("userId", "10009");
        map.put("nickname", "小师妹");
        map.put("strangerQuestion", "你喜欢去看蔚蓝的大海还是去爬巍峨的高山");
        map.put("reply", "我喜欢秋天的落叶，夏天的泉水，冬天的雪地，只要有你一切皆可~");
        String msg = JSON.toJSONString(map);
        System.out.println(msg);
        huanXinTemplate.sendMsg("1",msg);*/
    }
}
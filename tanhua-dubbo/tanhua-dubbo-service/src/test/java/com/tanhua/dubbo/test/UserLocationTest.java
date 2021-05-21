package com.tanhua.dubbo.test;


import com.tanhua.dubbo.api.UserLocationApi;
import org.apache.dubbo.config.annotation.Reference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserLocationTest {
    @Reference
    private UserLocationApi userLocationApi;

    @Test
    public void addLocation(){
    /*    userLocationApi.addLocation(113.929778,22.582111,"深圳黑马程序员",1l);
        userLocationApi.addLocation(113.925528,22.587995,"红荔村肠粉",2l);
        userLocationApi.addLocation(113.93814,22.562578,"深圳南头直升机场",3l);
        userLocationApi.addLocation(114.064478,22.549528,"深圳市政府",4l);
        userLocationApi.addLocation(113.986074,22.547726,"欢乐谷",5l);
        userLocationApi.addLocation(113.979399,22.540746,"世界之窗",6l);
        userLocationApi.addLocation(114.294924,22.632275,"东部华侨城",7l);
        userLocationApi.addLocation(114.314011,22.598196,"大梅沙海滨公园",8l);
        userLocationApi.addLocation(113.821705,22.638172,"深圳宝安国际机场",9l);
        userLocationApi.addLocation(113.912386,22.566223,"海雅缤纷城(宝安店)",10l);*/
    }
}
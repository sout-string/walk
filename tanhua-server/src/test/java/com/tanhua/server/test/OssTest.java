package com.tanhua.server.test;

import com.tanhua.commons.templates.OssTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @author : 涂根
 * @date : 2021/05/04 下午 8:29
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class OssTest {
    @Autowired
    private OssTemplate ossTemplate;

    @Test
    public void testOss() throws FileNotFoundException {
/*        FileInputStream is = new FileInputStream("C:\\Users\\涂根\\Desktop\\Snipaste_2021-04-28_11-30-57.png");
        ossTemplate.upload("图片2.png", is);*/
    }
}

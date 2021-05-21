package com.tanhua.server.test;

import com.tanhua.commons.templates.FaceTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * @author : TuGen
 * @date : 2021/5/6 17:01
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class FaceTest {
    @Autowired
    private FaceTemplate faceTemplate;

    @Test
    public void testFace() throws IOException {
     /*   boolean isHumanFace = faceTemplate.detect(Files.readAllBytes(new File("C:\\Users\\TuGen\\Pictures\\Saved Pictures\\OIP.jpg").toPath()));
        System.out.println("isHumanFace = " + isHumanFace);*/
    }

}

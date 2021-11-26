package cn.edu.lingnan.mooc.core.controller;

import cn.edu.lingnan.mooc.core.BaseMvcTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author xmz
 * @Date: 2020/10/06
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class CourseControllerTest extends BaseMvcTest {

    @Test
    public void testHelloWord(){
        String resultJson = sendGet("/admin/course/hello", null).isOk().getResultJson();
        System.out.println(resultJson);
    }


}

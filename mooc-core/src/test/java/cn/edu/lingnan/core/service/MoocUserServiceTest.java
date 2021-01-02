package cn.edu.lingnan.core.service;

import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author xmz
 * @date: 2021/01/02
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class MoocUserServiceTest {

    @Autowired
    private MoocUserService moocUserService;
    @Test
    public void test(){
        moocUserService.getUserMap(Lists.newArrayList(1,2,3))
                .forEach((k,v) -> System.out.println("k=" + k + ",v=" + v));
    }
}

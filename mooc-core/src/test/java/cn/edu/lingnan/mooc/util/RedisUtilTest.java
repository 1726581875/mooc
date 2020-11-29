package cn.edu.lingnan.mooc.util;

import cn.edu.lingnan.core.entity.Role;
import cn.edu.lingnan.core.util.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

/**
 * @author xmz
 * @date: 2020/11/16
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class RedisUtilTest {

    @Test
    public void testSet(){
        String key = "xmz_test";
        RedisUtil.set(key,"我是xiaomingzhang",60L);
        String value = RedisUtil.get(key);
        System.out.println(value);
    }

    @Test
    public void setObjectTest(){
        RedisUtil.set("role",new Role());
        Role role = RedisUtil.get("role", Role.class);
        Assert.notNull(role,"Redis取对象不能为null");
        System.out.println(role);
    }

    @Test
    public void testGetToken(){
        String userToken = RedisUtil.get("d613fb3f-2eb1-438f-afe5-51d1a57064b0");
        System.out.println(userToken);
    }

}

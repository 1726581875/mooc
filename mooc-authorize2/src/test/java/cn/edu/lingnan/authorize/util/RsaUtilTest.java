package cn.edu.lingnan.authorize.util;

import cn.edu.lingnan.mooc.common.util.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author xmz
 * @date: 2020/11/22
 */
@SpringBootTest
public class RsaUtilTest {

    @Value("${mooc.rsa.privateKey}")
    private String PRI_KEY;

    @Value("${mooc.rsa.publicKey}")
    private String PUB_KEY;

    private String password = "xmz";

    @Test
    public void test() throws Exception {

        String encryptResult = RsaUtil.encryptByPublicKey(PUB_KEY, password);
        String decryptResult = RsaUtil.decryptByPrivateKey(PRI_KEY, encryptResult);
        System.out.println(decryptResult);
    }

    @Test
    public void getOnlineUser(){
        Set keys = RedisUtil.getRedisTemplate().keys("online_*");
        keys.forEach(System.out::println);
    }

    @Test
    public void testTimer(){
        System.out.println("创建定时任务");
        Timer timer = new Timer();
    }

}

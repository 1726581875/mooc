package cn.edu.lingnan.mooc.redis;

import cn.edu.lingnan.mooc.entity.UserToken;
import cn.edu.lingnan.mooc.util.RedisUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import java.time.Duration;

/**
 * @author xmz
 * @date: 2020/11/16
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class RedisTest {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void redisSetAndGetTest() throws JsonProcessingException {
       // redisTemplate.opsForValue().set("xmz","lsx");
        String token = "aa.bb.cc";
        UserToken userToken = new UserToken();
        userToken.setToken(token);
        userToken.setSessionId("aaaa");
        userToken.setPermission("aaabbbcccddd");
        userToken.setUserId(1);
        redisTemplate.opsForValue().setIfAbsent(token, objectMapper.writeValueAsString(userToken), Duration.ofMinutes(1L));

        String str = redisTemplate.opsForValue().get(token);
        System.out.println(str);

        UserToken userToken1 = objectMapper.readValue(str, UserToken.class);
        System.out.println(userToken1);

    }


    @Test
    public void redisUtilTest(){
        String key = "xmz_test_1";
        String value = "hahaha";
        RedisUtil.set(key,value);
        String getValue = RedisUtil.get(key);
        Assert.isTrue(getValue.equals(value),"获取到的值必须与原来的相同");
    }









}

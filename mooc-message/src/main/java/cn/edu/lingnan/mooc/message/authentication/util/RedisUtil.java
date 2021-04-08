package cn.edu.lingnan.mooc.message.authentication.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @author xmz
 * @date: 2020/11/16
 * 封装redis基本操作
 */
public class RedisUtil {

    private static StringRedisTemplate redisTemplate = SpringContextHolder.getBean(StringRedisTemplate.class);
    /**
     * ObjectMapper这个类进行json与对象之间的转换
     */
    private static ObjectMapper objectMapper = SpringContextHolder.getBean(ObjectMapper.class);

    private static final Logger log = LoggerFactory.getLogger(RedisUtil.class);
    /**
     * 默认缓存2小时
     */
    private static final long DEFAULT_EXPIRE_TIME = 7200L;

    public static RedisTemplate getRedisTemplate(){
        return redisTemplate;
    }

    public static String get(String key){
        return redisTemplate.opsForValue().get(key);
    }

    public static <T> T get(String key,Class<T> clazz){
        String content = redisTemplate.opsForValue().get(key);
        if(content == null){
            return null;
        }
        try {
           T obj = objectMapper.readValue(content, clazz);
            return obj;
        } catch (JsonProcessingException e){
            log.error("解析json时出了错",e);
        }
        return null;
    }

    public static void set(String key, Object obj){
        set(key,obj,DEFAULT_EXPIRE_TIME,TimeUnit.SECONDS);
    }

    public static void set(String key, Object obj, long seconds){
        set(key,obj,seconds,TimeUnit.SECONDS);
    }

    public static void set(String key, Object obj, long expire, TimeUnit timeUni){
        try {
            set(key, objectMapper.writeValueAsString(obj), expire, timeUni);
        } catch (JsonProcessingException e) {
            log.error("{}对象转换为json字符串时出错",obj,e);
        }
    }


    public static void set(String key, String value){
        set(key,value,DEFAULT_EXPIRE_TIME);
    }

    public static void set(String key, String value,long seconds){
        set(key,value,seconds,TimeUnit.SECONDS);
    }

    public static void set(String key, String value,long expire,TimeUnit timeUnit){
        redisTemplate.opsForValue().set(key,value, expire, timeUnit);
    }



    public static Boolean setIfAbsent(String key, String value){
        return setIfAbsent(key, value, DEFAULT_EXPIRE_TIME);
    }

    public static Boolean setIfAbsent(String key, String value,long seconds){
        return setIfAbsent(key,value,seconds,TimeUnit.SECONDS);
    }

    public static Boolean setIfAbsent(String key, String value, long expire, TimeUnit timeUnit){
        return redisTemplate.opsForValue().setIfAbsent(key,value,expire,timeUnit);
    }

    public static Boolean delete(String key){
        return redisTemplate.delete(key);
    }

    public static long getExpire(String key){
        return redisTemplate.getExpire(key);
    }

    public static long getExpire(String key,TimeUnit timeUnit){
        return redisTemplate.getExpire(key,timeUnit);
    }

    public static boolean isExist(String key){
        return redisTemplate.hasKey(key);
    }

    public static boolean isNotExist(String key){
        return !redisTemplate.hasKey(key);
    }

}

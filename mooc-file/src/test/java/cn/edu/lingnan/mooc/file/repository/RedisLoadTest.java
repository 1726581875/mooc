package cn.edu.lingnan.mooc.file.repository;

import cn.edu.lingnan.mooc.file.authentication.util.RedisUtil;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.StringUtils;

public class RedisLoadTest {


    @Autowired
    private DataSourceTransactionManager transactionManager;


    @Autowired
    private StringRedisTemplate redisTemplate;
    //用来输出日志
    private final Logger logger = LoggerFactory.getLogger(RedisLoadTest.class);

    /**
     * setIfAbsent 如果redis里不存在key，则设置并返回true，否则不设置返回false
     * getAndSet
     * @param key
     * @param value  当前时间+超时时间
     * @return
     */
    public boolean lock(String key, String value){
        //如果可以成功设置,返回true
        if(redisTemplate.opsForValue().setIfAbsent(key, value)){
            return true;
        }
        //获取当前时间戳
        String currentTime = redisTemplate.opsForValue().get(key);
        //如果锁过期
        if(currentTime != null && Long.parseLong(currentTime)<System.currentTimeMillis()){
            //获取上一个锁的时间
            String oldTime =  redisTemplate.opsForValue().getAndSet(key,value);
            if(oldTime != null && oldTime.equals(currentTime)){
                return true;
            }
        }

        //否则返回false
        return false;
    }

    public void unlock(String key,String value){
        try{
            String currentValue = redisTemplate.opsForValue().get(key);
            if(!StringUtils.isEmpty(currentValue) && currentValue.equals(value)){
                redisTemplate.opsForValue().getOperations().delete(key);
            }
        }catch (Exception e) {
            logger.error("===解锁时发生了异常======",e);
        }

    }

    @Autowired
    private TransactionUtils transactionUtils;
    /**
     * 对于每个活动
     * 1、尝试去获取锁
     * 1.1 若锁已经被其他线程占有，直接返回
     * 1。2 若成功获取到锁，并且活动剩余人数减1，执行入库操作（事务保障数据一致性）
     * 最后操作完成，释放锁
     */
    @Test
    @Transactional
    public void test() {

        String key = "1";

        //当前时间加超时时间
        long time = System.currentTimeMillis() + 1000;
        //加锁并判断
        if (!lock(key, String.valueOf(time))) {
            System.out.println("人太多了,请再试试");
        }
        try {


            DefaultTransactionDefinition def = new DefaultTransactionDefinition();
            // explicitly setting the transaction name is something that can only be done programmatically
            def.setName("SomeTxName");
            def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
            TransactionStatus status = transactionManager.getTransaction(def);


            //秒杀逻辑
            //1、减库存
            //2、插入订单
            transactionManager.commit(status);
        } catch (Exception e) {
            logger.error("===入库发生了异常======",e);
        } finally {
            //释放锁
            unlock(key, String.valueOf(time));
        }


        System.out.println("11111111");
    }


    public void todoSomeThing(){




    }

    /**
     * 方案2，使用redis的incrBy的原子性
     * incrBy原子性递增或递减操作，会返回递增或递减后的值
     * 如果原来的值是 10 ，执行incrBy -1 会返回9
     *
     *
     */
    @Test
    public void test2(){

        String key = "key";

        if(redisTemplate.opsForValue().increment(key, -1) >= 0){
            //做插入订单操作

        }else {
            if(redisTemplate.opsForValue().get(key) != null) {
                redisTemplate.opsForValue().set(key, "0");
            }
        }


    }





}

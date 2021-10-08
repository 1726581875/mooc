package cn.edu.lingnan.mooc.statistics.dao.repository;

import cn.edu.lingnan.mooc.statistics.model.entity.mysql.LoginAmountCount;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author xmz
 * @date: 2021/03/27
 */
@SpringBootTest
public class LoginAmountCountRepositoryTest {

    @Autowired
    private LoginAmountCountRepository loginAmountCountRepository;

    /**
     * 补全登录人数统计，如果缺了则补 （统计登录人数是随机生成的，是模拟数据）
     */
    @Test
    public void addLoginAmountCount(){

        int beforeDay = 10;
        Date beforeTime = getBeforeTime(beforeDay);
        //获取数据库若干天前的数据
        List<LoginAmountCount> loginAmountCounts = loginAmountCountRepository.findLoginAmountCountByTime(beforeTime, new Date());
        Map<String,LoginAmountCount> countDateMap = new HashMap<>();
        if(!CollectionUtils.isEmpty(loginAmountCounts)){
            loginAmountCounts.forEach(e -> countDateMap.put(e.getCountTime(),e));
        }
        List<LoginAmountCount> saveList = new ArrayList<>(beforeDay);
        for (int i = 1; i <= beforeDay; i++){
            //创建一个统计记录
            LoginAmountCount loginAmountCount = createLoginAmountCount(i);
            //如果数据库存在当天的统计数据不需要新增，否则就是保存新建的这个
            saveList.add(countDateMap.getOrDefault(loginAmountCount.getCountTime(),loginAmountCount));
        }
        System.out.println("=============save list ============");
        saveList.forEach(System.out::println);
        loginAmountCountRepository.saveAll(saveList);
        System.out.println("=============save list end =================");
    }

    private LoginAmountCount createLoginAmountCount(int beforeDay){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date beforeTime = getBeforeTime(beforeDay);
        LoginAmountCount loginAmountCount = new LoginAmountCount();
        loginAmountCount.setAmount(new Random().nextInt(100));
        loginAmountCount.setCountTime(simpleDateFormat.format(beforeTime));
        loginAmountCount.setCreateTime(beforeTime);
        return loginAmountCount;
    }

    private Date getBeforeTime(int beforeDay){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, -beforeDay);
        return calendar.getTime();
    }
}

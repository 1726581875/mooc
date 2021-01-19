package cn.edu.lingnan.mooc.statistics.job;
import cn.edu.lingnan.mooc.statistics.entity.mysql.LoginAmountCount;
import cn.edu.lingnan.mooc.statistics.repository.LoginAmountCountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

/**
 * @author xmz
 * @date: 2021/01/10
 */
@Component
public class LoginUserCountJob {

    @Autowired
    private LoginAmountCountRepository loginAmountCountRepository;

    /**
     * 定时任务统计前一天登录人数
     * 每天凌晨1点执行
     */
    @Scheduled(cron = "0 0 1 * * ?")
    //@Scheduled(cron = "0/5 * * * * ? ")
    public void countLoginPersonAmount(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH,-1);
        // 前一天的0点
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date beginTime = calendar.getTime();


        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(new Date());
        endCalendar.add(Calendar.DAY_OF_MONTH,-1);
        endCalendar.set(Calendar.HOUR_OF_DAY, 23);
        endCalendar.set(Calendar.MINUTE, 59);
        endCalendar.set(Calendar.SECOND, 59);
        Date endTime = endCalendar.getTime();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        System.out.println("beginTime=" + simpleDateFormat1.format(beginTime));
        System.out.println("endTime=" + simpleDateFormat1.format(endTime));

        int count = loginAmountCountRepository.LoginPersonCountByTime(beginTime, endTime);

        LoginAmountCount loginAmountCount = new LoginAmountCount();
        loginAmountCount.setAmount(count);
        loginAmountCount.setUpdateTime(new Date());
        loginAmountCount.setCreateTime(new Date());
        loginAmountCount.setCountTime(beginTime);
        loginAmountCountRepository.save(loginAmountCount);
    }

    /**
     * 统计登录人数补偿任务
     *
     */
   // @Scheduled(cron = "0/5 * * * * ? ")
    @Scheduled(cron = "0 0 2 * * ?")
    public void countLoginAmountCompensate(){

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH,-1);
        // 前一天的0点
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date beginTime = calendar.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");
        LoginAmountCount loginAmountCount = new LoginAmountCount();
        loginAmountCount.setCountTime(beginTime);
        Optional<LoginAmountCount> amountCount = loginAmountCountRepository.findOne(Example.of(loginAmountCount));
        if(!amountCount.isPresent()){
            this.countLoginPersonAmount();
        }

    }



}

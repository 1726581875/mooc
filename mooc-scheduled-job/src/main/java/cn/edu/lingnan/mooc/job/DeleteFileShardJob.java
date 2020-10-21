package cn.edu.lingnan.mooc.job;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author xmz
 * @date: 2020/10/21
 */
@Component
public class DeleteFileShardJob {

    @Scheduled(cron = "0/5 * * * * ? ")
    public void testTask(){
        System.out.println("定时任务执行了");
    }

}

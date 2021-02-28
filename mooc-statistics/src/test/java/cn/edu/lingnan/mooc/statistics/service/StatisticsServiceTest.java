package cn.edu.lingnan.mooc.statistics.service;

import cn.edu.lingnan.mooc.statistics.entity.StatisticsVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

/**
 * @author xmz
 * @date: 2021/02/28
 */
@SpringBootTest
public class StatisticsServiceTest {

    @Autowired
    private StatisticsService statisticsService;

    /**
     * 获取课程收藏数趋势、观看人数趋势 测试
     */
    @Test
    public void getCollectionAndViewTrendTest(){
        Map<String, List<StatisticsVO>> lastWeekMap = statisticsService.getCollectionAndViewTrend("lastWeek");
        lastWeekMap.forEach((k,v) -> {
            System.out.println("============"+  k + "==================");
            v.forEach(System.out::println);
        });

    }


}

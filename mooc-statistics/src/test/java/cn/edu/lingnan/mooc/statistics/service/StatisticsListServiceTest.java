package cn.edu.lingnan.mooc.statistics.service;


import cn.edu.lingnan.mooc.common.model.PageVO;
import cn.edu.lingnan.mooc.statistics.entity.CourseRecordStatisticsVO;
import cn.edu.lingnan.mooc.statistics.entity.StatisticsListViewQuery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author xmz
 * @date 2021/04/06
 */
@SpringBootTest
public class StatisticsListServiceTest {

    @Autowired
    private StatisticsListService statisticsListService;

    @Test
    public void getCourseStatisticsListTest(){
        //构造查询条件
        StatisticsListViewQuery statisticsListViewQuery = new StatisticsListViewQuery();
        statisticsListViewQuery.setBeginTime("2020-12-12");
        statisticsListViewQuery.setEndTime("2021-4-6");
        statisticsListViewQuery.setIsAsc(false);
        statisticsListViewQuery.setCurrPage(1);
        statisticsListViewQuery.setPageSize(10);
        //调用查询方法
        PageVO<CourseRecordStatisticsVO> courseStatisticsListPage = statisticsListService.getCourseStatisticsList(statisticsListViewQuery);
        List<CourseRecordStatisticsVO> courseRecordStatisticsVOList = courseStatisticsListPage.getContent();
        //打印输出
        courseRecordStatisticsVOList.forEach(System.out::println);
    }



}

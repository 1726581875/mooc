package cn.edu.lingnan.mooc.statistics.service;


import cn.edu.lingnan.mooc.common.model.PageVO;
import cn.edu.lingnan.mooc.statistics.model.vo.CourseRecordStatisticsVO;
import cn.edu.lingnan.mooc.statistics.model.vo.StatisticsListViewQuery;
import cn.edu.lingnan.mooc.statistics.model.vo.TopCourseVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
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
        statisticsListViewQuery.setOrderField("viewNum");
        //调用查询方法
        PageVO<CourseRecordStatisticsVO> courseStatisticsListPage = statisticsListService.getCourseStatisticsList(statisticsListViewQuery);
        List<CourseRecordStatisticsVO> courseRecordStatisticsVOList = courseStatisticsListPage.getContent();
        //打印输出
        courseRecordStatisticsVOList.forEach(System.out::println);
    }

    /**
     * 根据某一字段查询前十课程测试
     */
    @Test
    public void findCourseTop10Test(){
        List<TopCourseVO> collectionNum = statisticsListService.listTop10CourseByField(0L, new Date().getTime(), "collectionNum",0);
        collectionNum.forEach(System.out::println);
    }



}

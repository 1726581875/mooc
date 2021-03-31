package cn.edu.lingnan.mooc.statistics.controller;

import cn.edu.lingnan.mooc.statistics.BaseMvcTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author xmz
 * @date: 2021/01/10
 */
@SpringBootTest
public class MonitorControllerTest extends BaseMvcTest {

    /**
     * 测试获取周/月人数
     */
    @Test
    public void countWeekOrMonthPersonTest(){
        System.out.println("=====测试获取最近一周登录人数接口=====");
        String weekResultJson = sendGet("/monitor/countLoginUser", null).isOk().getResultJson();
        System.out.println(weekResultJson);
        System.out.println("=====测试获取最近一月登录人数接口=====");
        String monthResultJson = sendGet("/monitor/countLoginUser?type=month", null).isOk().getResultJson();
        System.out.println(monthResultJson);
    }

    /**
     * 测试获取周/月人数
     */
    @Test
    public void countWeekOrMonthCourseTest(){
        System.out.println("=====测试获取最近一周新增课程数接口=====");
        String weekResultJson = sendGet("/monitor/countNewCoursecountNewCourse", null).isOk().getResultJson();
        System.out.println(weekResultJson);
        System.out.println("=====测试获取最近一月新增课程数接口=====");
        String monthResultJson = sendGet("/monitor/countNewCourse?type=month", null).isOk().getResultJson();
        System.out.println(monthResultJson);
    }


}

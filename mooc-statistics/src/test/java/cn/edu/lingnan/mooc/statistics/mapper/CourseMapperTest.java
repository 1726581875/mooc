package cn.edu.lingnan.mooc.statistics.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author xmz
 * @date: 2021/02/22
 */
@SpringBootTest
public class CourseMapperTest {

    @Resource
    private CourseMapper courseMapper;

    /**
     * mysql统计课程分类测试
     */
    @Test
    public void selectAllTest(){
        //课程分类
        System.out.println("===============sql 2===============");
        courseMapper.countAllCourseCategory()
                .forEach(map -> {
                    map.forEach((k, v)->System.out.println("k=" + k + ",v=" + v));
                });
        //课程分类 by teacherId
        System.out.println("===============sql 3===============");
        Integer teacherId = 7;
        courseMapper.countCourseCategoryByUserId(teacherId)
                .forEach(map -> {
                    map.forEach((k, v)->System.out.println("k=" + k + ",v=" + v));
                });
        //统计新增课程数
        System.out.println("===============sql 4===============");
        courseMapper.countNewAddCourseNum(new Date(),new Date())
                .forEach(map -> {
                    map.forEach((k, v)->System.out.println("k=" + k + ",v=" + v));
                });

    }


    /**
     * 测试统计评论数
     */
    @Test
    public void countCommentGroupByTimeTest() {
        //开始时间七天前
        Date startTime = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_WEEK, -7);
        //统计全部评论
        List<Map<String, Object>> mapList = courseMapper.countCommentGroupByTime(null,calendar.getTime(),new Date());
        mapList.forEach(map -> {
            map.forEach((k, v)->System.out.println("k=" + k + ",v=" + v));
        });

        //统计teacherId =7的所属课程的全部评论
        Integer teacherId = 7;
        List<Map<String, Object>> mapList2 = courseMapper.countCommentGroupByTime(teacherId,calendar.getTime(),new Date());
        mapList2.forEach(map -> {
            map.forEach((k, v)->System.out.println("k=" + k + ",v=" + v));
        });


    }
}

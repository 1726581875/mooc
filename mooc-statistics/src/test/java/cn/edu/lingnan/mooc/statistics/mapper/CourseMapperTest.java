package cn.edu.lingnan.mooc.statistics.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author xmz
 * @date: 2021/02/22
 */
@SpringBootTest
public class CourseMapperTest {

    @Resource
    private CourseMapper courseMapper;

    @Test
    public void selectAllTest(){
        courseMapper.selectAll().forEach(System.out::println);
        System.out.println("===============sql 2===============");
        courseMapper.countAllCourseCategory()
                .forEach(map -> {
                    map.forEach((k, v)->System.out.println("k=" + k + ",v=" + v));
                });
        System.out.println("===============sql 3===============");
        Integer teacherId = 7;
        courseMapper.countCourseCategoryByUserId(teacherId)
                .forEach(map -> {
                    map.forEach((k, v)->System.out.println("k=" + k + ",v=" + v));
                });

        System.out.println("===============sql 4===============");
        courseMapper.countNewAddCourseNum(new Date(),new Date())
                .forEach(map -> {
                    map.forEach((k, v)->System.out.println("k=" + k + ",v=" + v));
                });

    }


}

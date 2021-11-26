package cn.edu.lingnan.mooc.core.service;

import cn.edu.lingnan.mooc.core.entity.Course;
import cn.edu.lingnan.mooc.core.enums.CourseEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * @author xmz
 * @date: 2021/03/27
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class CourseServiceTest {

    @Autowired
    private CourseService courseService;

    private static final Logger log = LoggerFactory.getLogger(CourseServiceTest.class);

    /**
     * 一周内新增课程
     */
    @Test
    public void createCourseInWeek(){
        for (int i=0 ; i < 10; i ++) {
            courseService.insert(createCourse());
        }
    }


    private Course createCourse(){
        Course course = new Course();
        course.setCollectionNum(0);
        course.setLearningNum(0);
        course.setCommentNum(0);
        course.setTeacherId(7L);
        course.setDuration(0);
        course.setImage("/file/default.png");
        course.setName("课程-" + UUID.randomUUID().toString().substring(0,10));
        course.setSummary("summary");
        course.setStatus(CourseEnum.NORMAL.getStatus());
        course.setCreateTime(getRandomTime(7));
        return course;
    }

    private Date getRandomTime(int beforeDay){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Random random = new Random();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, -random.nextInt(beforeDay + 1));
       log.info("=====random a time {}====",simpleDateFormat.format(calendar.getTime()));
        return calendar.getTime();
    }


}

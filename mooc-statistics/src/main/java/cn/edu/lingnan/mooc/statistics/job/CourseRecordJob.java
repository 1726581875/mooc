package cn.edu.lingnan.mooc.statistics.job;

import cn.edu.lingnan.mooc.statistics.entity.es.CourseRecord;
import cn.edu.lingnan.mooc.statistics.entity.es.EsCourse;
import cn.edu.lingnan.mooc.statistics.entity.mysql.Course;
import cn.edu.lingnan.mooc.statistics.repository.CourseRepository;
import cn.edu.lingnan.mooc.statistics.repository.es.EsCourseRepository;
import cn.edu.lingnan.mooc.statistics.util.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xmz
 * @date: 2021/04/14
 * 课程统计定时任务
 */
@Component
public class CourseRecordJob {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EsCourseRepository esCourseRepository;
    @Autowired
    private CourseRepository mysqlCourseRepository;
    @Autowired
    private IdWorker idWorker;

    /**
     * 每天凌晨2:30分开始统计课程数据，统计的是前一天的数据
     */
    @Scheduled(cron = "0 30 2 * * ?")
    public void courseSyncJob(){


        List<Course> courseList = courseRepository.findAll();


        courseList.forEach(course->{
            CourseRecord courseRecord = new CourseRecord();
            courseRecord.setId(idWorker.nextId());
            courseRecord.setCourseId(course.getId());
            courseRecord.setCollectionNum(null);
            courseRecord.setTeacherId(course.getTeacherId());
            courseRecord.setViewNum(null);
            courseRecord.setCreateTime(new Date());
        });


    }



    private EsCourse createEsCourse(Course course){
        EsCourse esCourse = new EsCourse();
        esCourse.setId(course.getId());
        esCourse.setName(course.getName());
        esCourse.setSummary(course.getSummary());
        esCourse.setTeacherId(course.getTeacherId());
        esCourse.setCreateTime(course.getCreateTime());
        esCourse.setUpdateTime(course.getUpdateTime());
        return esCourse;
    }



}

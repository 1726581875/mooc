package cn.edu.lingnan.mooc.statistics.job;

import cn.edu.lingnan.mooc.statistics.entity.es.EsCourse;
import cn.edu.lingnan.mooc.statistics.entity.mysql.Course;
import cn.edu.lingnan.mooc.statistics.repository.CourseRepository;
import cn.edu.lingnan.mooc.statistics.repository.es.EsCourseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xmz
 * @date: 2021/04/14
 * 课程信息同步到es
 */
@Slf4j
@Component
public class CourseSyncJob {

    @Autowired
    private EsCourseRepository esCourseRepository;
    @Autowired
    private CourseRepository mysqlCourseRepository;

    @Scheduled(cron = "0 0/5 * * * ?")
    public void courseSyncJob(){
        log.info("============ 课程同步任务开始 ============");
        // 获取mysql全部课程信息
        List<Course> courseList = mysqlCourseRepository.findAll();
        // 转换为es存储对象
        List<EsCourse> esCourseList = courseList.stream().map(this::createEsCourse).collect(Collectors.toList());
        // 批量插入
        esCourseRepository.saveAll(esCourseList);
        log.info("=========== 课程同步任务结束 完成数={} ==========",esCourseList);
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

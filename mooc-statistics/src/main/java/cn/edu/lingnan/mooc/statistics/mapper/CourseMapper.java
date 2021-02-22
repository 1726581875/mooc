package cn.edu.lingnan.mooc.statistics.mapper;

import cn.edu.lingnan.mooc.statistics.entity.mysql.Course;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 * @author xmz
 * @date: 2021/02/22
 */
public interface CourseMapper {

     List<Course> selectAll();

     List<Map<String,Object>> countAllCourseCategory();

     /**
      * 根据教师id，统计创建的课程对应分类个数
      * @param userId
      * @return
      */
     List<Map<String,Object>> countCourseCategoryByUserId(Integer userId);

     /**
      * 统计新增课程数
      * @return
      */
     List<Map<String,Object>> countNewAddCourseNum();
}

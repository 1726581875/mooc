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

     List<Map<String,Object>> countCourseCategoryByUserId(Integer userId);
}

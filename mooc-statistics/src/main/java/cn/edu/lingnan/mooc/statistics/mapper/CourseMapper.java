package cn.edu.lingnan.mooc.statistics.mapper;

import cn.edu.lingnan.mooc.statistics.entity.mysql.Course;
import org.apache.ibatis.annotations.Param;

import java.math.BigInteger;
import java.util.Date;
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
     List<Map<String,Object>> countNewAddCourseNum(@Param("beginTime") Date beginTime,@Param("endTime") Date endTime);

     /**
      * 统计评论数
      * @param teacherId
      * @param beginTime
      * @param endTime
      * @return
      */
     List<Map<String,Object>> countCommentGroupByTime(@Param("teacherId")Integer teacherId,@Param("beginTime") Date beginTime,@Param("endTime") Date endTime);

     /**
      * 根据课程id获取课程名
      * @param courseIdList
      * @return
      */
     List<Map<String,Object>> getCourseNameByIdList(@Param("courseIdList") List<Integer> courseIdList);


     /**
      * 根据课程id获取课程
      * @param courseIdList
      * @return
      */
     List<Course> getCourseByIdList(@Param("courseIdList") List<Integer> courseIdList);

     /**
      * 根据教师id获取教师名
      * @param teacherIdList
      * @return
      */
     List<Map<String,Object>> getTeacherNameByIdList(@Param("teacherIdList") List<Integer> teacherIdList);

}

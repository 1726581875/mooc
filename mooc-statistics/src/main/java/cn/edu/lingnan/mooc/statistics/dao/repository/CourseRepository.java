package cn.edu.lingnan.mooc.statistics.dao.repository;

import cn.edu.lingnan.mooc.statistics.model.entity.mysql.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author xmz
 * @date 2020/10/31
 * @see JpaRepository 支持常用增删查改
 * @see JpaSpecificationExecutor 支持多条件分页
 */
public interface CourseRepository extends JpaRepository<Course, Integer>,JpaSpecificationExecutor<Course> {
    /**
     * 查询所有的课程ID
     * @return
     */
    @Query(value="select id from course",nativeQuery=true)
    List<Integer> findAllCourseId();
}

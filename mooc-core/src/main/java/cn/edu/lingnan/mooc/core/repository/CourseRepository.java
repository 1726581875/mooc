package cn.edu.lingnan.mooc.core.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import cn.edu.lingnan.mooc.core.model.entity.Course;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author xmz
 * @date 2020/10/31
 * @see JpaRepository 支持常用增删查改
 * @see JpaSpecificationExecutor 支持多条件分页
 */
public interface CourseRepository extends JpaRepository<Course, Long>,JpaSpecificationExecutor<Course> {

    /**
     * 根据标签id查询课程
     * @param tagIdList 标签id List
     * @param pageable 分页参数
     * @return
     */
    @Query(value="select c.* from course c,course_tag_rel r where c.id = r.course_id and c.status=1 and r.tag_id in (?1) group by c.id"
            ,countQuery = "select count(c.id) from course c,course_tag_rel r where c.id = r.course_id and c.status=1 and r.tag_id in (?1) group by c.id",nativeQuery=true)
    Page<Course> findCourseByTagList(List<Long> tagIdList, Pageable pageable);

}

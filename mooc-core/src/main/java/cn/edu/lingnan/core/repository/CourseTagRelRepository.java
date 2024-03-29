package cn.edu.lingnan.core.repository;
import cn.edu.lingnan.core.entity.CourseTagRel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author xmz
 * @date 2020/12/10
 * @see JpaRepository 支持常用增删查改
 * @see JpaSpecificationExecutor 支持多条件分页
 */
public interface CourseTagRelRepository extends JpaRepository<CourseTagRel, Integer>,JpaSpecificationExecutor<CourseTagRel> {

    /**
     * 根据课程id删除标签关系
     * @param courseId
     */
    void deleteAllByCourseId(Integer courseId);

}

package cn.edu.lingnan.mooc.repository;
import cn.edu.lingnan.mooc.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author xmz
 * @date 2020/10/06
 * @see JpaRepository 支持常用增删查改
 * @see JpaSpecificationExecutor 支持多条件分页
 */
public interface CourseRepository extends JpaRepository<Course, Integer>,JpaSpecificationExecutor<Course> {

}

package cn.edu.lingnan.core.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import cn.edu.lingnan.core.entity.Chapter;

/**
 * @author xmz
 * @date 2020/10/31
 * @see JpaRepository 支持常用增删查改
 * @see JpaSpecificationExecutor 支持多条件分页
 */
public interface ChapterRepository extends JpaRepository<Chapter, Integer>,JpaSpecificationExecutor<Chapter> {

}

package cn.edu.lingnan.mooc.file.repository;
import cn.edu.lingnan.mooc.file.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author xmz
 * @date 2020/10/31
 * @see JpaRepository 支持常用增删查改
 * @see JpaSpecificationExecutor 支持多条件分页
 */
public interface SectionRepository extends JpaRepository<Section, Integer>,JpaSpecificationExecutor<Section> {

}

package cn.edu.lingnan.mooc.core.repository;
import cn.edu.lingnan.mooc.core.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author xmz
 * @date 2020/12/05
 * @see JpaRepository 支持常用增删查改
 * @see JpaSpecificationExecutor 支持多条件分页
 */
public interface CategoryRepository extends JpaRepository<Category, Integer>,JpaSpecificationExecutor<Category> {

}

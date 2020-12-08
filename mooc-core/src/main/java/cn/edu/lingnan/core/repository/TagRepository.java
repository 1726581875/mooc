package cn.edu.lingnan.core.repository;
import cn.edu.lingnan.core.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author xmz
 * @date 2020/12/05
 * @see JpaRepository 支持常用增删查改
 * @see JpaSpecificationExecutor 支持多条件分页
 */
public interface TagRepository extends JpaRepository<Tag, Integer>,JpaSpecificationExecutor<Tag> {

    List<Tag> findAllByCategoryId(Integer categoryId);

    List<Tag> findAllByCategoryIdIn(List<Integer> categoryIdList);

}

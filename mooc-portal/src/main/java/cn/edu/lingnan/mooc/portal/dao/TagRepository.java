package cn.edu.lingnan.mooc.portal.dao;

import cn.edu.lingnan.mooc.portal.model.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author xmz
 * @date 2021/09/30
 * @see JpaRepository 支持常用增删查改
 * @see JpaSpecificationExecutor 支持多条件分页
 */
public interface TagRepository extends JpaRepository<Tag, Integer>, JpaSpecificationExecutor<Tag> {

    List<Tag> findAllByCategoryIdIn(List<Integer> categoryIdList);

}

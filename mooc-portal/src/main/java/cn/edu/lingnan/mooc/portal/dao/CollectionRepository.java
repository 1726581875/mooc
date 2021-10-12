package cn.edu.lingnan.mooc.portal.dao;


import cn.edu.lingnan.mooc.portal.model.entity.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author xiaomingzhang
 * @data 2021/02/02
 * 收藏表repository
 */
public interface CollectionRepository extends JpaRepository<Collection, Long>, JpaSpecificationExecutor<Collection> {
}

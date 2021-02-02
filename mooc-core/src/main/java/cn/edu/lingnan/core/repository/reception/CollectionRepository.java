package cn.edu.lingnan.core.repository.reception;

import cn.edu.lingnan.core.entity.reception.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author xiaomingzhang
 * @data 2021/02/02
 * 收藏表repository
 */
public interface CollectionRepository extends JpaRepository<Collection, Integer>, JpaSpecificationExecutor<Collection> {
}

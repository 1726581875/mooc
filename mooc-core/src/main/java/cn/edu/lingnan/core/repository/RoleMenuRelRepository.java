package cn.edu.lingnan.core.repository;

import cn.edu.lingnan.core.entity.RoleMenuRel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author xmz
 * @date: 2020/11/08
 */
public interface RoleMenuRelRepository extends JpaRepository<RoleMenuRel, Integer>, JpaSpecificationExecutor<RoleMenuRel> {

     @Transactional
     void deleteAllByRoleIdIn(List<Integer> roleIdList);

}

package cn.edu.lingnan.authorize.dao.jpa;

import cn.edu.lingnan.authorize.model.entity.RoleMenuRel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author xmz
 * @date: 2020/11/08
 */
public interface RoleMenuRelRepository extends JpaRepository<RoleMenuRel, Long>, JpaSpecificationExecutor<RoleMenuRel> {

     @Transactional
     void deleteAllByRoleIdIn(List<Long> roleIdList);

}

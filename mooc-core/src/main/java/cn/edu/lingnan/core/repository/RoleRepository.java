package cn.edu.lingnan.core.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import cn.edu.lingnan.core.entity.Role;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author xmz
 * @date 2020/10/31
 * @see JpaRepository 支持常用增删查改
 * @see JpaSpecificationExecutor 支持多条件分页
 */
public interface RoleRepository extends JpaRepository<Role, Integer>,JpaSpecificationExecutor<Role> {

    @Query(value = "select r.* from role r, manager_role_rel mr where r.id = mr.role_id and mr.manager_id = ?1",nativeQuery=true)
    List<Role> findAllRoleInManagerId(Integer managerId);
}

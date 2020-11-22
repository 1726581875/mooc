package cn.edu.lingnan.mooc.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import cn.edu.lingnan.mooc.model.ManagerRoleRel;

/**
 * @author xmz
 * @date 2020/10/31
 * @see JpaRepository 支持常用增删查改
 * @see JpaSpecificationExecutor 支持多条件分页
 */
public interface ManagerRoleRelRepository extends JpaRepository<ManagerRoleRel, Integer>,JpaSpecificationExecutor<ManagerRoleRel> {

}

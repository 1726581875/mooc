package cn.edu.lingnan.core.repository;
import cn.edu.lingnan.core.entity.MoocManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author xmz
 * @date 2020/10/31
 * @see JpaRepository 支持常用增删查改
 * @see JpaSpecificationExecutor 支持多条件分页
 */
public interface ManagerRepository extends JpaRepository<MoocManager, Integer>,JpaSpecificationExecutor<MoocManager> {

}

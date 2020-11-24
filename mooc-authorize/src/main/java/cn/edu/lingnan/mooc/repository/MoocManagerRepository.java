package cn.edu.lingnan.mooc.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import cn.edu.lingnan.mooc.entity.MoocManager;

/**
 * @author xmz
 * @date 2020/10/25
 * @see JpaRepository 支持常用增删查改
 * @see JpaSpecificationExecutor 支持多条件分页
 */
public interface MoocManagerRepository extends JpaRepository<MoocManager, Integer>,JpaSpecificationExecutor<MoocManager> {

}

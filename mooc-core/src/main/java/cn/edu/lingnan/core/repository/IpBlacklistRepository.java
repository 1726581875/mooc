package cn.edu.lingnan.core.repository;
import cn.edu.lingnan.core.entity.IpBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author xmz
 * @date 2021/03/23
 * @see JpaRepository 支持常用增删查改
 * @see JpaSpecificationExecutor 支持多条件分页
 */
public interface IpBlacklistRepository extends JpaRepository<IpBlacklist, Integer>,JpaSpecificationExecutor<IpBlacklist> {

}

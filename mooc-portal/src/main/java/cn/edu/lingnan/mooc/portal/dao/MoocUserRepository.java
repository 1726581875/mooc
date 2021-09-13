package cn.edu.lingnan.mooc.portal.dao;
import cn.edu.lingnan.mooc.portal.authorize.model.entity.MoocUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author xmz
 * @date 2020/10/31
 * @see JpaRepository 支持常用增删查改
 * @see JpaSpecificationExecutor 支持多条件分页
 */
public interface MoocUserRepository extends JpaRepository<MoocUser, Integer>,JpaSpecificationExecutor<MoocUser> {

}

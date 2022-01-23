package cn.edu.lingnan.mooc.authorize.dao.jpa;

import cn.edu.lingnan.mooc.authorize.model.entity.MoocManager;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author xiaomingzhang
 * @date 2022/1/23
 */
public interface ManagerRepository extends JpaRepository<MoocManager,Long> {
}

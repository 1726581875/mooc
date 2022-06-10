package cn.edu.lingnan.mooc.authorize.dao.jpa;

import cn.edu.lingnan.mooc.authorize.model.entity.LoginLog;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author xiaomingzhang
 * @date 2022/6/2
 */
public interface LoginLogRepository extends JpaRepository<LoginLog,Long> {

}

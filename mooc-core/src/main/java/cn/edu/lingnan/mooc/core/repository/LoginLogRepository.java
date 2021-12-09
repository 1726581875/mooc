package cn.edu.lingnan.mooc.core.repository;
import cn.edu.lingnan.mooc.core.model.entity.LoginLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * @author xmz
 * @date 2020/10/31
 * @see JpaRepository 支持常用增删查改
 * @see JpaSpecificationExecutor 支持多条件分页
 */
public interface LoginLogRepository extends JpaRepository<LoginLog, Integer>,JpaSpecificationExecutor<LoginLog> {


    /**
     * 分页条件查询
     * @param matchStr 匹配字符串
     * @param startTime
     * @param endTime
     * @param pageable
     * @return
     */
    @Query(value="select * from login_log where create_time >= ?2 and create_time <= ?3 "
            + " and if(?1 is null , 1=1 ,(account = ?1 or log_name = ?1))"
            ,countQuery = "select count(1) from login_log  where create_time >= ?2 and create_time <= ?3 "
            + " and if(?1 is null , 1=1 ,(account = ?1 or log_name = ?1))",nativeQuery=true)
    Page<LoginLog> findLoginLogByCondition(String matchStr, Date startTime, Date endTime, Pageable pageable);


    /**
     * 条件查询,返回List
     * @param matchStr 匹配字符串
     * @param startTime
     * @param endTime
     * @return
     */
    @Query(value="select * from login_log where create_time >= ?2 and create_time <= ?3 "
            + " and if(?1 is null , 1=1 ,(account = ?1 or log_name = ?1)) "
            + " order by create_time desc "
            ,nativeQuery=true)
    List<LoginLog> findLoginLogByCondition(String matchStr, Date startTime, Date endTime);
}
